import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface Discovery extends Remote {
    boolean register(String url, Ledger server) throws RemoteException;

    Map<String, Ledger> list() throws RemoteException;
}
