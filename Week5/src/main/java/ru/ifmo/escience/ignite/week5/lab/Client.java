package ru.ifmo.escience.ignite.week5.lab;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Client
{
    @QuerySqlField(index = true)
    private final long clientId;
    @QuerySqlField(index = true)
    private final long brokerId;
    @QuerySqlField(index = true)
    private final String firstName;
    @QuerySqlField(index = true)
    private final String secondName;
    @QuerySqlField(index = true)
    private final double amount;

    public Client(long clientId, long brokerId, String firstName, String secondName, double amount) {
        this.clientId = clientId;
        this.brokerId = brokerId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.amount = amount;
    }

    public long getClientId() {
        return clientId;
    }

    public long getBrokerId() {
        return brokerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public double getAmount() {
        return amount;
    }

    public Long getKey()
    {
        return clientId;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", brokerId=" + brokerId +
                ", firstName=" + firstName +
                ", secondName=" + secondName +
                ", amount=" + amount +
                '}';
    }
}
