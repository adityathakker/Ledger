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
//        System.out.println(String.format("Append Called with Entry: %s", entry.toString()));
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            writer.write(String.format("%d,%s,%s\n", entry.getTimestamp(), entry.getServerId(), entry.getValue()));
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Log getLogFile() {
        return this;
    }

    @Override
    public LogEntry getLatestLog() {
        List<LogEntry> logs= this.getAllLogs();
        if(logs.size() == 0) {
            return null;
        }
        return logs.get(logs.size()-1);
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

}
