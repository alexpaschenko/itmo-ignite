package ru.ifmo.escience.ignite.week5.lab;

/**
 * Let's assume this class is really unreachable.
 * Nobody can put annotations here and this class is very important of course.
 */
public class BankAccount {
    private final int id;
    private final int bankId;
    private final String ccy;
    private final double amount;

    public BankAccount(int id, int bankId, String ccy, double amount) {
        this.id = id;
        this.bankId = bankId;
        this.ccy = ccy;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getBankId() {
        return bankId;
    }

    public String getCcy() {
        return ccy;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", bankId=" + bankId +
                ", ccy='" + ccy + '\'' +
                ", amount=" + amount +
                '}';
    }
}
