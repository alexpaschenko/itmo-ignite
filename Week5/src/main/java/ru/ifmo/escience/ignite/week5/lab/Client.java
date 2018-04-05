package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Client {
    @QuerySqlField(index = true)
    private final int id;
    @QuerySqlField
    private final String fullName;
    @QuerySqlField
    @AffinityKeyMapped
    private final int bankAccountId;


    public Client(int id, String fullName, int bankAccountId) {
        this.id = id;
        this.fullName = fullName;
        this.bankAccountId = bankAccountId;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public int getBankAccountId() {
        return bankAccountId;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", bankAccountId=" + bankAccountId +
                '}';
    }
}
