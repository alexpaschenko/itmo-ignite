package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Bank {
    @QuerySqlField(index = true)
    private final long bankClientId;
    @QuerySqlField
    private final String bankOffice;

    public Bank(long bankClientId, String bankOffice) {
        this.bankClientId = bankClientId;
        this.bankOffice = bankOffice;
    }

    public long getBankClientId() {
        return bankClientId;
    }

    public String getBankOffice() {
        return bankOffice;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bankClientId=" + bankClientId +
                ", bankOffice='" + bankOffice + '\'' +
                '}';
    }
}
