import log.Log;
import paxos.Election;
import paxos.Paxos;

public interface Ledger extends Log, Paxos, Discovery, Election {
}
