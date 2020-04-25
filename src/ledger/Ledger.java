package ledger;

import ledger.log.Log;
import ledger.log.LogEntry;
import ledger.paxos.Election;
import ledger.paxos.Paxos;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Ledger extends Paxos, Discovery, Election, Remote {
    String getAddress() throws RemoteException;

    boolean append(final LogEntry entry) throws RemoteException, MalformedURLException, NotBoundException;

    Log getLogFile() throws RemoteException;

    LogEntry getLatestLog() throws RemoteException;

    List<LogEntry> getAllLogs() throws RemoteException;

}
