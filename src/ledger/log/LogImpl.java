package ledger.log;

import java.util.List;

public class LogImpl implements Log {

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
}
