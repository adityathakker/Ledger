package ledger.paxos;

import ledger.log.LogEntry;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Paxos extends Remote {
    Promise propose(final double proposalId, final LogEntry lastAcceptedLogEntry) throws RemoteException;

    Commitment accept(final double proposalId, final LogEntry logEntry) throws RemoteException;

    boolean learn(final double proposalId, final LogEntry logEntry) throws RemoteException;
}
