package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class ClientKey {
    @QuerySqlField
    private final int id;
    @QuerySqlField
    @AffinityKeyMapped
    private final BankAccountKey bankAccountKey;

    public ClientKey(int id, BankAccountKey bankAccountKey) {
        this.id = id;
        this.bankAccountKey = bankAccountKey;
    }

    public int getId() {
        return id;
    }

    public BankAccountKey getBankAccountKey() {
        return bankAccountKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientKey clientKey = (ClientKey) o;

        if (id != clientKey.id) return false;
        return bankAccountKey != null ? bankAccountKey.equals(clientKey.bankAccountKey) : clientKey.bankAccountKey == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (bankAccountKey != null ? bankAccountKey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClientKey{" +
                "id=" + id +
                ", bankAccountKey=" + bankAccountKey +
                '}';
    }
}
