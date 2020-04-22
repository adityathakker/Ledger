package paxos;

import log.LogEntry;

import java.util.Objects;

public class Promise {
    private String serverId;
    private long proposalId;
    private LogEntry lastAcceptedLogEntry;

    public Promise(final String serverId, final long proposalId, final LogEntry lastAcceptedLogEntry) {
        this.serverId = serverId;
        this.proposalId = proposalId;
        this.lastAcceptedLogEntry = lastAcceptedLogEntry;
    }

    public Promise(String serverId, long proposalId) {
        this.serverId = serverId;
        this.proposalId = proposalId;
    }

    public String getServerId() {
        return serverId;
    }

    public long getProposalId() {
        return proposalId;
    }

    public LogEntry getLastAcceptedLogEntry() {
        return lastAcceptedLogEntry;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Promise)) return false;
        Promise promise = (Promise) o;
        return getProposalId() == promise.getProposalId() &&
                getServerId().equals(promise.getServerId()) &&
                Objects.equals(getLastAcceptedLogEntry(), promise.getLastAcceptedLogEntry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerId(), getProposalId(), getLastAcceptedLogEntry());
    }

    @Override
    public String toString() {
        return "Promise{" +
                "serverId='" + serverId + '\'' +
                ", proposalId=" + proposalId +
                ", lastAcceptedLogEntry=" + lastAcceptedLogEntry +
                '}';
    }
}
