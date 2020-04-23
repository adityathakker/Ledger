import log.Log;
import log.LogEntry;
import paxos.Commitment;
import paxos.Election;
import paxos.Paxos;
import paxos.Promise;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class LedgerImpl extends UnicastRemoteObject implements Log, Paxos, Discovery, Election {

    protected LedgerImpl(final int port) throws RemoteException {
        super(port);
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
    public boolean register(final String url, final LedgerImpl server) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, LedgerImpl> list() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLeader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean forceMyLeadership(String serverId) {
        throw new UnsupportedOperationException();
    }
}
