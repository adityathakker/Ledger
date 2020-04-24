import com.google.common.base.Preconditions;
import ledger.DiscoveryUtil;
import ledger.Ledger;
import ledger.LedgerConstants;
import ledger.LedgerImpl;
import ledger.log.LogEntry;
import ledger.paxos.ElectionUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class LedgerServer {
    public static void main(String[] args) {
        System.setProperty("sun.rmi.transport.connectionTimeout", "100");

        if (args.length != 1) {
            System.out.println("./Usage java LedgerServer [4-digit-port]\n");
            return;
        }
        final int port = Integer.parseInt(args[0]);

        try {
            final Registry registry = LocateRegistry.createRegistry(port);

            final Ledger ledger = new LedgerImpl(InetAddress.getLocalHost().getHostName(), port, registry);
            final String myAddress = String.format(LedgerConstants.URL_FORMAT, InetAddress.getLocalHost().getHostName(), port);
            Naming.rebind(myAddress, ledger);

            // register this server with discoveryNode
            if (!DiscoveryUtil.registerWithDiscoveryNode(myAddress, ledger)) {
                System.out.println("Registration with Discovery Node Failed!");
                System.exit(1);
            }

            // force my leadership over others. Eventually, the best leader will be elected
            // as it will force it's leadership upon others
            if (!ElectionUtil.bestLeaderForAll()) {
                System.out.println("Setting Leadership Failed!");
            }

            // once leadership and registry things are over, server is ready
            System.out.println("Server Ready!");

            //handles restart of a server after it is down.

            InetSocketAddress addr = DiscoveryUtil.getDiscoveryNode();
            final String discoveryNodeAddress = String.format(LedgerConstants.URL_FORMAT, addr.getHostName(), addr.getPort());
            final Ledger discoveryLedger = (Ledger)Naming.lookup(discoveryNodeAddress);

            LogEntry latestLog = ledger.getLatestLog();
            List<LogEntry> discoveryAllLogs = discoveryLedger.getAllLogs();

            // find entry where the server went down

            int i=0;
            for(i = 0;i<discoveryAllLogs.size();i++){
                if(latestLog.equals(discoveryAllLogs.get(i))) {
                    i++;
                    break;
                }
            }

            if(ledger.getLatestLog()!=discoveryLedger.getLatestLog()){
                for(;i<discoveryLedger.getAllLogs().size();i++) {
                    ledger.getLogFile().append(discoveryAllLogs.get(i));
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


}
