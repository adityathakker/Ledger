package ledger.paxos;

import ledger.log.LogEntry;

import java.rmi.RemoteException;

public class PaxosImpl implements Paxos {
    @Override
    public Promise propose(final long proposalId, final LogEntry lastAcceptedLogEntry) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Commitment accept(final long proposalId, final LogEntry logEntry) throws RemoteException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean learn(final long proposalId) throws RemoteException {
        throw new UnsupportedOperationException();
    }
}
