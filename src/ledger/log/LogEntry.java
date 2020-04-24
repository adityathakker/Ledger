package ledger.log;

import java.io.Serializable;
import java.util.Objects;

public class LogEntry implements Serializable {
    private static final long serialVersionUID = 14l;
    private long timestamp;
    private String serverId;
    private String value;

    public LogEntry(final String value, final String serverId) {
        this.timestamp = System.currentTimeMillis();
        this.value = value;
        this.serverId = serverId;
    }

    public LogEntry(long timestamp, String serverId, String value) {
        this.timestamp = timestamp;
        this.serverId = serverId;
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getServerId() {
        return serverId;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "timestamp=" + timestamp +
                ", serverId='" + serverId + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof LogEntry)) return false;
        LogEntry logEntry = (LogEntry) o;
        return getTimestamp() == logEntry.getTimestamp() &&
                getServerId().equals(logEntry.getServerId()) &&
                getValue().equals(logEntry.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimestamp(), getServerId(), getValue());
    }
}
