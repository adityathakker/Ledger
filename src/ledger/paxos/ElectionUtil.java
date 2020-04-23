package ledger.paxos;

import ledger.DiscoveryUtil;
import ledger.Ledger;
import ledger.LedgerConstants;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ElectionUtil {
    public static boolean setLeadershipToAll(String serverAddress) {
        System.out.println(String.format("Forcing %s leadership onto others", serverAddress));
        try {
            final InetSocketAddress discoveryAddress = DiscoveryUtil.getDiscoveryNode();
            if (discoveryAddress != null) {
                final Ledger discoveryLedger = (Ledger) Naming.lookup(String.format(LedgerConstants.URL_FORMAT, discoveryAddress.getHostName(), discoveryAddress.getPort()));
                for (String address : discoveryLedger.listServers()) {
                    final Ledger tempLedger = (Ledger) Naming.lookup(address);
                    tempLedger.setLeader(serverAddress);
                }
            }
            return true;
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            return false;
        }
    }
}
