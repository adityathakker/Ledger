package ledger;

import ledger.log.LogEntry;
import ledger.paxos.Election;
import ledger.paxos.Paxos;

import java.rmi.RemoteException;
import java.util.List;

public interface Ledger extends Paxos, Discovery, Election {
    boolean append(final LogEntry entry) throws RemoteException;

    LogEntry getLatestLog() throws RemoteException;

    LogEntry getLatestLog(final String serverId) throws RemoteException;

    List<LogEntry> getAllLogs() throws RemoteException;

    List<LogEntry> getAllLogs(final String serverId) throws RemoteException;

    List<LogEntry> getLogs(final int count) throws RemoteException;

    List<LogEntry> getLogs(final String serverId, final int count) throws RemoteException;

    List<LogEntry> getLogsBetween(final long startTimestamp, final long endTimestamp) throws RemoteException;

    List<LogEntry> getLogsBetween(final String serverId, final long startTimestamp, final long endTimestamp) throws RemoteException;
}
