package ledger;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

public final class DiscoveryUtil {
    public static InetSocketAddress getDiscoveryNode() {
        final Properties properties = new Properties();
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(LedgerConstants.CONFIG_PATH);
            properties.load(stream);
            final String address = properties.getProperty(LedgerConstants.CONFIG_DISCOVERY_PORT_KEY);
            final String[] tokens = address.split(":");
            return new InetSocketAddress(tokens[0], Integer.parseInt(tokens[1]));
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean registerWithDiscoveryNode(final String myAddress, final Ledger ledger) {
        final InetSocketAddress addr = getDiscoveryNode();
        if (addr == null)
            return false;

        final String discoveryNodeAddress = String.format(LedgerConstants.URL_FORMAT, addr.getHostName(), addr.getPort());
        if (discoveryNodeAddress.equals(myAddress)) {
            return true;
        }

        try {
            final Ledger discoveryLedger = (Ledger) Naming.lookup(discoveryNodeAddress);
            return discoveryLedger.registerServer(myAddress, ledger);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            return false;
        }
    }
}
