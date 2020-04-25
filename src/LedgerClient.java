import ledger.DiscoveryUtil;
import ledger.Ledger;
import ledger.LedgerConstants;
import ledger.log.LogEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class LedgerClient {
    public static void main(String[] args) throws IOException, NotBoundException {

        final InetSocketAddress discoveryNodeAddress = DiscoveryUtil.getDiscoveryNode();
        if (discoveryNodeAddress != null) {
            final Ledger discoveryNodeLedger = (Ledger) Naming.lookup(
                    String.format(LedgerConstants.URL_FORMAT,
                            discoveryNodeAddress.getHostName(),
                            discoveryNodeAddress.getPort()));

            System.out.println(" == Ledger - A Distributed Logging Service == ");
            printOptions();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String choice = reader.readLine();

            boolean exitFlag = false;
            while (!exitFlag) {
                switch (choice) {
                    case "1":
                        List<String> aliveServers = printAliveServers(discoveryNodeLedger);
                        System.out.println("Choose one of the servers above");
                        int serverChoice = 0;
                        try {
                            serverChoice = Integer.parseInt(reader.readLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a integer while selecting server");
                            break;
                        }
                        if (serverChoice > aliveServers.size()) {
                            System.out.println("Please enter a valid server index number");
                        } else {
                            System.out.println("Please enter the message you want to append to the log");
                            String message = reader.readLine();
                            try {
                                final Ledger tempLedger = (Ledger) Naming.lookup(aliveServers.get(serverChoice - 1));
                                tempLedger.append(new LogEntry(message, aliveServers.get(serverChoice - 1)));
                            } catch (ConnectException e) {
                                System.out.println(String.format("Server with address: %s is down", aliveServers.get(serverChoice - 1)));
                            }
                        }
                        break;
                    case "2":
                        aliveServers = printAliveServers(discoveryNodeLedger);
                        System.out.println("Choose one of the servers above");
                        serverChoice = 0;
                        try {
                            serverChoice = Integer.parseInt(reader.readLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a integer while selecting server");
                            break;
                        }
                        if (serverChoice > aliveServers.size()) {
                            System.out.println("Server index invalid");
                        } else {
                            try {
                                final Ledger tempLedger = (Ledger) Naming.lookup(aliveServers.get(serverChoice - 1));
                                List<LogEntry> log = tempLedger.getAllLogs();
                                for (int i = 0; i < log.size(); i++) {
                                    System.out.println(log.get(i));
                                }
                            } catch (ConnectException e) {
                                System.out.println(String.format("Server with address: %s is down", aliveServers.get(serverChoice - 1)));
                            }
                        }
                        break;
                    case "3":
                        exitFlag = true;
                        break;
                    default:
                        System.out.println("Invalid choice, Please select one of the two options");
                        break;
                }
                if (!exitFlag) {
                    printOptions();
                    choice = reader.readLine();
                }
            }
        }
    }

    private static void printOptions() {
        System.out.println("Choose one of the three options");
        System.out.println("1. Append");
        System.out.println("2. Get");
        System.out.println("3. Exit");
    }

    private static List<String> printAliveServers(Ledger discoveryNodeLedger) throws RemoteException {
        System.out.println("These are the Servers available");
        List<String> serverList = discoveryNodeLedger.listServers();
        for (int i = 0; i < serverList.size(); i++) {
            System.out.println(String.format("%d: %s", (i + 1), serverList.get(i)));
        }
        return serverList;
    }
}
