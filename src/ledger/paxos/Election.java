package ledger.paxos;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Election extends Remote {
    String getLeader() throws RemoteException;

    boolean setLeader(final String serverAddress) throws RemoteException;
}
