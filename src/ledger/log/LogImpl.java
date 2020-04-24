package ledger.log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogImpl implements Log {
    private final String path;

    public LogImpl(String path) throws IOException {
        this.path = path;
        final File logFile = new File(path);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
    }

    @Override
    public boolean append(final LogEntry entry) {
        System.out.println(String.format("Append Called with Entry: %s", entry.toString()));
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            writer.newLine();
            writer.write(String.format("%d,%s,%s", entry.getTimestamp(), entry.getServerId(), entry.getValue()));
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
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
        List<LogEntry> logs = new ArrayList<>();
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(
                    path));
            String line = reader.readLine();
            while (line != null) {
                if (line.length() <= 1)
                    continue;
                String[] tokens = line.split(",");
                logs.add(new LogEntry(Long.parseLong(tokens[0]), tokens[1], tokens[2]));
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            return null;
        }
        return logs;
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
