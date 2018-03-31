package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public class BankClientToAccount {
    @AffinityKeyMapped
    private final long ccyAccountId;
    private final long bankClientId;

    public BankClientToAccount(long ccyAccountId, long bankClientId) {
        this.ccyAccountId = ccyAccountId;
        this.bankClientId = bankClientId;
    }

    public long getCcyAccountId() {
        return ccyAccountId;
    }

    public long getBankClientId() {
        return bankClientId;
    }

    @Override
    public String toString() {
        return "BankClientToAccount{" +
                "ccyAccountId=" + ccyAccountId +
                ", bankClientId=" + bankClientId +
                '}';
    }
}
