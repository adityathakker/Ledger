package ledger;

import ledger.log.Log;
import ledger.log.LogEntry;
import ledger.log.LogImpl;
import ledger.paxos.Commitment;
import ledger.paxos.ElectionUtil;
import ledger.paxos.Promise;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class LedgerImpl extends UnicastRemoteObject implements Ledger {
    private final String hostname;
    private final int port;
    private final Registry registry;
    private Ledger currentLeader;
    private double currentProposalId;
    private double lastPromisedProposalId;
    private LogEntry lastAcceptedLogEntry;
    private Log log;

    public LedgerImpl(final String hostname, final int port, final Registry registry) throws IOException {
        super(port);
        this.hostname = hostname;
        this.port = port;
        this.registry = registry;
        this.currentLeader = this;
        this.currentProposalId = (System.currentTimeMillis() + port / 10000.0);
        this.lastAcceptedLogEntry = null;
        this.lastPromisedProposalId = this.currentProposalId;
        this.log = new LogImpl(port + ".txt");
    }

    @Override
    public String getAddress() throws RemoteException {
        return hostname + ":" + port;
    }

    private void updateCurrentProposalId() {
        this.currentProposalId = (System.currentTimeMillis() + port / 10000.0);
    }

    @Override
    public boolean append(final LogEntry entry) throws RemoteException, MalformedURLException, NotBoundException {
        if (!this.currentLeader.getAddress().equals(getAddress())) {
            System.out.println(String.format("Forwarding the operation to leader %s", this.currentLeader.getAddress()));
            return this.currentLeader.append(entry);
        }
        System.out.println("Append Operation Started");
        final InetSocketAddress discoveryAddress = DiscoveryUtil.getDiscoveryNode();
        final double proposalId = this.currentProposalId;
        updateCurrentProposalId();

        if (discoveryAddress == null) {
            return false;
        }

        List<String> promiseList = new ArrayList<>();
        final Ledger discoveryLedger = (Ledger) Naming.lookup(String.format(LedgerConstants.URL_FORMAT,
                discoveryAddress.getHostName(),
                discoveryAddress.getPort()));

        // proposal stage
        for (String address : discoveryLedger.listServers()) {
            if (address.equals(getAddress())) {
                continue;
            }
            final Ledger tempLedger = (Ledger) Naming.lookup(address);
            Promise current_Promise = tempLedger.propose(proposalId, this.lastAcceptedLogEntry);
            if (current_Promise != null) {
                promiseList.add(address);
            }
        }

        //todo: consider yourself as a participant in the count
        if (promiseList.size() < discoveryLedger.listServers().size() / 2) {
            System.out.println("Didn't get a majority in Proposal Phase");
            return false;
        }

        List<Commitment> commitmentList = new ArrayList<>();
        for (String address : promiseList) {
            final Ledger tempLedger = (Ledger) Naming.lookup(address);
            Commitment commitment = tempLedger.accept(proposalId, entry);
            if (commitment != null) {
                commitmentList.add(commitment);
            }
        }

        if (commitmentList.size() < discoveryLedger.listServers().size() / 2) {
            // announce the values to everyone.
            System.out.println("Didn't get a majority in Acceptance Phase");
            return false;
        }

        boolean result = true;
        for (String address : discoveryLedger.listServers()) {
            if (address.equals(getAddress())) {
                result = result & this.learn(proposalId, entry);
            }
            final Ledger tempLedger = (Ledger) Naming.lookup(address);
            result = result & tempLedger.learn(proposalId, entry);
        }
        return result;
    }

    @Override
    public LogEntry getLatestLog() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LogEntry getLatestLog(final String serverId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<LogEntry> getAllLogs() {
        return log.getAllLogs();
    }

    @Override
    public List<LogEntry> getAllLogs(final String serverId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<LogEntry> getLogs(final int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<LogEntry> getLogs(final String serverId, final int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<LogEntry> getLogsBetween(final long startTimestamp, final long endTimestamp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<LogEntry> getLogsBetween(final String serverId, final long startTimestamp, final long endTimestamp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Promise propose(final double proposalId, final LogEntry lastAcceptedLogEntry) throws RemoteException {
        System.out.println(String.format("Propose Called with ProposalId: %f and LastLogEntry:%s",
                proposalId, lastAcceptedLogEntry != null ? lastAcceptedLogEntry.toString() : "NULL"));
        if (this.currentProposalId >= proposalId) {
            return null;
        } else {
            this.lastPromisedProposalId = proposalId;
            if (this.lastAcceptedLogEntry == null) {
                return new Promise(this.getAddress(), proposalId);
            } else {
                return new Promise(this.getAddress(), proposalId, this.lastAcceptedLogEntry);
            }
        }
    }

    @Override
    public Commitment accept(final double proposalId, final LogEntry logEntry) throws RemoteException {
        System.out.println(String.format("Accept Called with ProposalId: %f and LogEntry:%s", proposalId, logEntry.toString()));
        if (this.currentProposalId >= proposalId || lastPromisedProposalId != proposalId) {
            return null;
        } else {
            this.lastAcceptedLogEntry = logEntry;
            return new Commitment(this.getAddress(), proposalId, logEntry);
        }
    }

    @Override
    public boolean learn(final double proposalId, final LogEntry logEntry) throws RemoteException {
        System.out.println(String.format("Learn Called with ProposalId: %f", proposalId));
        // append to log
        if (proposalId == lastPromisedProposalId) {
            this.lastAcceptedLogEntry = logEntry;
            return log.append(logEntry);
        } else {
            return false;
        }
    }

    @Override
    public boolean registerServer(final String url, final Ledger server) {
        try {
            this.registry.rebind(url, server);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    @Override
    public List<String> listServers() {
        try {
            final List<String> servers = new ArrayList<>();
            for (String host : this.registry.list()) {
                // weird localhost name by-default appears in all lists. So removing that
                if (host.equals(LedgerConstants.NAME))
                    continue;
                servers.add(host);
            }
            return servers;
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override
    public String getLeader() throws RemoteException {
        return this.currentLeader.getAddress();
    }

    @Override
    public boolean setLeader(final String serverAddress) throws RemoteException {
        try {
            // set the serverAddress as leader
            this.currentLeader = (Ledger) Naming.lookup(serverAddress);
            System.out.println(String.format("New Leader is %s", serverAddress));

            // check if my address is bigger than who I choose my leader as, if yes then force my leadership upon others
            if (getAddress().compareTo(this.currentLeader.getAddress()) > 0) {
                return ElectionUtil.setLeadershipToAll(getAddress());
            }

            return true;
        } catch (NotBoundException | MalformedURLException e) {
            return false;
        }
    }
}
