package ledger;

import ledger.log.LogEntry;
import ledger.paxos.Commitment;
import ledger.paxos.ElectionUtil;
import ledger.paxos.Promise;

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
    public boolean append(final LogEntry entry) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public Commitment accept(final long proposalId, final LogEntry logEntry) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean learn(final long proposalId) throws RemoteException {
        throw new UnsupportedOperationException();
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
            this.currentLeader = (Ledger) Naming.lookup(serverAddress);
            System.out.println(String.format("Setting %s as leader", serverAddress));

            if (getAddress().compareTo(this.currentLeader.getAddress()) > 0) {
                return ElectionUtil.setLeadershipToAll(getAddress());
            }

            return true;
        } catch (NotBoundException | MalformedURLException e) {
            return false;
        }
    }
}
