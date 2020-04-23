import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Discovery extends Remote {
    boolean registerServer(final String url, final Ledger server) throws RemoteException;

    List<String> listServers() throws RemoteException;
}
