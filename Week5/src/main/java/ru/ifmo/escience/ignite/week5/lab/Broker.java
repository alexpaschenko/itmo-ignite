package ru.ifmo.escience.ignite.week5.lab;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Broker
{
    @QuerySqlField(index = true)
    private final long brokerId;
    @QuerySqlField(index = true)
    private final long exchangeId;
    @QuerySqlField(index = true)
    private final long clientId;
    @QuerySqlField(index = true)
    private final String name;

    public Broker(long brokerId, long exchangeId, long clientId, String name)
    {
        this.brokerId = brokerId;
        this.exchangeId = exchangeId;
        this.clientId = clientId;
        this.name = name;
    }

    public long getbrokerId() {
        return brokerId;
    }

    public long getExchangeId() {
        return exchangeId;
    }

    public long getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public Long getKey()
    {
        return brokerId;
    }

    @Override
    public String toString() {
        return "Client{" +
                "brokerId=" + brokerId +
                ", exchangeId=" + exchangeId +
                ", clientId=" + clientId +
                ", name=" + name +
                '}';
    }
}