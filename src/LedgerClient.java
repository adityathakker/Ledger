import ledger.DiscoveryUtil;
import ledger.Ledger;
import ledger.LedgerConstants;

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
            System.out.println(discoveryNodeLedger.listServers());
        }

    }
}
