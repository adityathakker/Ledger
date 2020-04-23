package ledger.paxos;

import ledger.log.LogEntry;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Paxos extends Remote {
    Promise propose(final long proposalId, final LogEntry lastAcceptedLogEntry) throws RemoteException;

    Commitment accept(final long proposalId, final LogEntry logEntry) throws RemoteException;

    boolean learn(final long proposalId, LogEntry entry) throws RemoteException;
}
