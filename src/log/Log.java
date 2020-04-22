package log;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This interface defines operations that our log should be able to perform.
 */
public interface Log extends Remote {
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
