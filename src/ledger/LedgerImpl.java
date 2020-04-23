package ledger;

import ledger.log.LogEntry;
import ledger.paxos.Commitment;
import ledger.paxos.ElectionUtil;
import ledger.paxos.Promise;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class LedgerImpl extends UnicastRemoteObject implements Ledger {
    private final String hostname;
    private final int port;
    private final Registry registry;
    private Ledger currentLeader;
    private long currentId;
    private LogEntry lastAcceptedLogEntry;

    public LedgerImpl(final String hostname, final int port, final Registry registry) throws RemoteException {
        super(port);
        this.hostname = hostname;
        this.port = port;
        this.registry = registry;
        this.currentLeader = this;
    }

    @Override
    public String getAddress() throws RemoteException {
        return hostname + ":" + port;
    }

    @Override
    public long getCurrentId() throws RemoteException {
        return currentId;
    }

    @Override
    public void setCurrentId(long id) throws RemoteException {
        this.currentId = id;
    }

    @Override
    public LogEntry getLastAcceptedLogEntry() throws RemoteException {
        return lastAcceptedLogEntry;
    }

    @Override
    public boolean append(final LogEntry entry) throws RemoteException, MalformedURLException, NotBoundException {
        final InetSocketAddress discoveryAddress = DiscoveryUtil.getDiscoveryNode();
        long proposalId = this.getCurrentId()*2 + 1;

        if (discoveryAddress != null) {

            ArrayList<Promise> promiseList = new ArrayList<>();
            final Ledger discoveryLedger = (Ledger) Naming.lookup(String.format(LedgerConstants.URL_FORMAT,
                    discoveryAddress.getHostName(),
                    discoveryAddress.getPort()));

            // proposal stage
            for (String address : discoveryLedger.listServers()) {
                final Ledger tempLedger = (Ledger) Naming.lookup(address);
                Promise current_Promise = tempLedger.propose(proposalId, tempLedger.getLastAcceptedLogEntry());
                if(current_Promise!=null) {
                    promiseList.add(current_Promise);
                }
            }

            if (promiseList.size() < discoveryLedger.listServers().size() / 2) {
                return false;
            } else {
                // send accept
                ArrayList<Commitment> commitmentList = new ArrayList<>();

                List<String> promiseAddressList = promiseList.stream().map(Promise::getServerId).collect(Collectors.toList());
                for (String port : promiseAddressList) {
                    final Ledger tempLedger = (Ledger) Naming.lookup(String.format("rmi://%s:%s/Ledger",
                            DiscoveryUtil.getDiscoveryNode().getHostName(),port));
                    Commitment commitment = tempLedger.accept(proposalId, entry);
                    if (commitment != null) {
                        commitmentList.add(commitment);
                    }
                }

                if (commitmentList.size() > discoveryLedger.listServers().size() / 2) {
                    // announce the values to everyone.
                    for (String address : discoveryLedger.listServers()) {
                        final Ledger tempLedger = (Ledger) Naming.lookup(address);
                        tempLedger.learn(proposalId, entry);
                    }
                } else {
                    return false;
                }

            }
        }
//        throw new UnsupportedOperationException();
        return false;
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
        throw new UnsupportedOperationException();
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
    public Promise propose(final long proposalId, final LogEntry lastAcceptedLogEntry) throws RemoteException {
        if (this.getCurrentId() > proposalId) {
            return null;
        } else {
            this.currentId = proposalId;
            if (this.lastAcceptedLogEntry == null) {
                return new Promise(this.getAddress(), proposalId);
            } else {
                return new Promise(this.getAddress(), proposalId, this.getLastAcceptedLogEntry());
            }
        }
    }

    @Override
    public Commitment accept(final long proposalId, final LogEntry logEntry) throws RemoteException {
        if (this.getCurrentId() > proposalId) {
            return null;
        } else {
            this.lastAcceptedLogEntry = logEntry;
            return new Commitment(this.getAddress(), proposalId, logEntry);
        }
    }

    @Override
    public boolean learn(final long proposalId, LogEntry entry) throws RemoteException {
        this.currentId = proposalId;
        this.lastAcceptedLogEntry = entry;
        // append to log
        return true;
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
