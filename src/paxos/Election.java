package paxos;

public interface Election {
    String getLeader();

    boolean forceMyLeadership(String serverId);
}
