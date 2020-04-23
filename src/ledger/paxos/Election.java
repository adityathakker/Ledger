package ledger.paxos;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Election extends Remote {
    String getLeader() throws RemoteException;

    boolean forceMyLeadership(final String serverId) throws RemoteException;
}
