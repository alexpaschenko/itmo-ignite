package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class ClientKey {
    @QuerySqlField
    private final int id;
    @QuerySqlField
    @AffinityKeyMapped
    private final int bankAccountId;

    public ClientKey(int id, int bankAccountId) {
        this.id = id;
        this.bankAccountId = bankAccountId;
    }

    public int getId() {
        return id;
    }

    public int getBankAccountId() {
        return bankAccountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientKey clientKey = (ClientKey) o;

        if (id != clientKey.id) return false;
        return bankAccountId == clientKey.bankAccountId;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + bankAccountId;
        return result;
    }

    @Override
    public String toString() {
        return "ClientKey{" +
                "id=" + id +
                ", bankAccountId=" + bankAccountId +
                '}';
    }
}
