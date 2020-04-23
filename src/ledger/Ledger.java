package ledger;

import ledger.log.LogEntry;
import ledger.paxos.Election;
import ledger.paxos.Paxos;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Ledger extends Paxos, Discovery, Election, Remote {
    String getAddress() throws RemoteException;

    LogEntry getLastAcceptedLogEntry() throws RemoteException;

    long getCurrentId() throws RemoteException;

    void setCurrentId(long id) throws RemoteException;

    boolean append(final LogEntry entry) throws RemoteException, MalformedURLException, NotBoundException;

    LogEntry getLatestLog() throws RemoteException;

    LogEntry getLatestLog(final String serverId) throws RemoteException;

    List<LogEntry> getAllLogs() throws RemoteException;

    List<LogEntry> getAllLogs(final String serverId) throws RemoteException;

    List<LogEntry> getLogs(final int count) throws RemoteException;

    List<LogEntry> getLogs(final String serverId, final int count) throws RemoteException;

    List<LogEntry> getLogsBetween(final long startTimestamp, final long endTimestamp) throws RemoteException;

    List<LogEntry> getLogsBetween(final String serverId, final long startTimestamp, final long endTimestamp) throws RemoteException;
}
