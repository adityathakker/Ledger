import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class LedgerServer {

    public LedgerServer(String port) {
        System.setProperty("sun.rmi.transport.connectionTimeout", "100");
        try {
            Ledger ledger = new LedgerImpl(Integer.parseInt(port));
            LocateRegistry.createRegistry(Integer.parseInt(port));
            Naming.rebind("rmi://127.0.0.1:" + port + "/LedgerService", ledger);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("./Usage java Server 1099[4-digit-port] \n");
            return;
        }
        new LedgerServer(args[0]);
    }
}
