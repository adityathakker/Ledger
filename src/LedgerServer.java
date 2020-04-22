import log.LogEntry;
import paxos.Commitment;
import paxos.Paxos;
import paxos.Promise;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LedgerServer extends UnicastRemoteObject implements Paxos {

    protected LedgerServer(final int port) throws RemoteException {
        super(port);
    }

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
