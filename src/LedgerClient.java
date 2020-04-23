import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class LedgerClient {
    public static void main(String args[]) throws RemoteException, NotBoundException, MalformedURLException {
        Ledger ledgerClient = (Ledger) Naming.lookup("rmi://localhost:" + args[0] + "/LedgerService");
    }
}
