package ledger.log;

import java.util.List;

/**
 * This interface defines operations that our log should be able to perform.
 */
public interface Log {
    boolean append(final LogEntry entry);

    Log getLogFile();

    LogEntry getLatestLog();

    List<LogEntry> getAllLogs();

}
