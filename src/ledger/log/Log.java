package ledger.log;

import java.util.List;

/**
 * This interface defines operations that our log should be able to perform.
 */
public interface Log {
    boolean append(final LogEntry entry);

    LogEntry getLatestLog();

    LogEntry getLatestLog(final String serverId);

    List<LogEntry> getAllLogs();

    List<LogEntry> getAllLogs(final String serverId);

    List<LogEntry> getLogs(final int count);

    List<LogEntry> getLogs(final String serverId, final int count);

    List<LogEntry> getLogsBetween(final long startTimestamp, final long endTimestamp);

    List<LogEntry> getLogsBetween(final String serverId, final long startTimestamp, final long endTimestamp);
}
