package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Client
{
    @QuerySqlField
    private final long clientId;
    @QuerySqlField
    private final long brokerId;
    @QuerySqlField
    private final String firstName;
    @QuerySqlField
    private final String secondName;
    @QuerySqlField
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
}
