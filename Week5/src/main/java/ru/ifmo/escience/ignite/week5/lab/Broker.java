package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Broker
{
    @QuerySqlField
    private final long brokerId;
    @QuerySqlField
    private final long exchangeId;
    @QuerySqlField
    private final long clientId;
    @QuerySqlField
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
}