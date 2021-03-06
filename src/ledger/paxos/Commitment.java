package ledger.paxos;

import ledger.log.LogEntry;

import java.io.Serializable;
import java.util.Objects;

public class Commitment implements Serializable {
    private static final long serialVersionUID = 16l;

    private String serverId;
    private double proposalId;
    private LogEntry logEntry;

    public Commitment(final String serverId, final double proposalId, final LogEntry logEntry) {
        this.serverId = serverId;
        this.proposalId = proposalId;
        this.logEntry = logEntry;
    }

    public String getServerId() {
        return serverId;
    }

    public double getProposalId() {
        return proposalId;
    }

    public LogEntry getLogEntry() {
        return logEntry;
    }

    @Override
    public String toString() {
        return "Commitment{" +
                "serverId='" + serverId + '\'' +
                ", proposalId=" + proposalId +
                ", logEntry=" + logEntry +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Commitment)) return false;
        Commitment that = (Commitment) o;
        return getProposalId() == that.getProposalId() &&
                getServerId().equals(that.getServerId()) &&
                getLogEntry().equals(that.getLogEntry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerId(), getProposalId(), getLogEntry());
    }
}
