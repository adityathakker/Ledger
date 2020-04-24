import ledger.DiscoveryUtil;
import ledger.Ledger;
import ledger.LedgerConstants;
import ledger.log.LogEntry;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class LedgerClient {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, UnknownHostException {

        final InetSocketAddress discoveryNodeAddress = DiscoveryUtil.getDiscoveryNode();
        if (discoveryNodeAddress != null) {
            final Ledger discoveryNodeLedger = (Ledger) Naming.lookup(
                    String.format(LedgerConstants.URL_FORMAT,
                            discoveryNodeAddress.getHostName(),
                            discoveryNodeAddress.getPort()));
            for (String address : discoveryNodeLedger.listServers()) {
                final Ledger tempLedger = (Ledger) Naming.lookup(address);
                tempLedger.append(new LogEntry(address + "123", address));
            }

            for (String address : discoveryNodeLedger.listServers()) {
                System.out.println("Printing from Address: " + address);
                final Ledger tempLedger = (Ledger) Naming.lookup(address);
                System.out.println(tempLedger.getAllLogs());
                System.out.println("\n");
            }
        }

    }
}
