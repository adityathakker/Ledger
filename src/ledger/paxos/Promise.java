package ledger.paxos;

import ledger.log.LogEntry;

import java.io.Serializable;
import java.util.Objects;

public class Promise implements Serializable {
    private static final long serialVersionUID = 15l;

    private String serverId;
    private double proposalId;
    private LogEntry lastAcceptedLogEntry;

    public Promise(final String serverId, final double proposalId, final LogEntry lastAcceptedLogEntry) {
        this.serverId = serverId;
        this.proposalId = proposalId;
        this.lastAcceptedLogEntry = lastAcceptedLogEntry;
    }

    public Promise(String serverId, double proposalId) {
        this.serverId = serverId;
        this.proposalId = proposalId;
    }

    public String getServerId() {
        return serverId;
    }

    public double getProposalId() {
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
