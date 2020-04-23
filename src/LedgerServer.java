import ledger.DiscoveryUtil;
import ledger.Ledger;
import ledger.LedgerConstants;
import ledger.LedgerImpl;
import ledger.paxos.ElectionUtil;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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

            if (!DiscoveryUtil.registerWithDiscoveryNode(myAddress, ledger)) {
                System.out.println("Registration with Discovery Node Failed!");
            }

            if (!ElectionUtil.setLeadershipToAll(myAddress)) {
                System.out.println("Setting Leadership Failed!");
            }
            System.out.println("Server Ready!");
        } catch (Exception e) {
            System.out.println("Trouble: " + e.getMessage());
        }
    }


}
