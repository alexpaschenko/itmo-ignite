package ru.ifmo.escience.ignite.week5.lab;

/**
 * Let's assume this class is really unreachable.
 * Nobody can put annotations here and this class is very important of course.
 */
public class BankAccount {
    private final BankAccountKey bankAccountKey;
    private final String ccy;
    private final double amount;

    public BankAccount(BankAccountKey bankAccountKey, String ccy, double amount) {
        this.bankAccountKey = bankAccountKey;
        this.ccy = ccy;
        this.amount = amount;
    }

    public BankAccountKey getBankAccountKey() {
        return bankAccountKey;
    }

    public String getCcy() {
        return ccy;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankAccount that = (BankAccount) o;

        if (Double.compare(that.amount, amount) != 0) return false;
        if (bankAccountKey != null ? !bankAccountKey.equals(that.bankAccountKey) : that.bankAccountKey != null)
            return false;
        return ccy != null ? ccy.equals(that.ccy) : that.ccy == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = bankAccountKey != null ? bankAccountKey.hashCode() : 0;
        result = 31 * result + (ccy != null ? ccy.hashCode() : 0);
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "bankAccountKey=" + bankAccountKey +
                ", ccy='" + ccy + '\'' +
                ", amount=" + amount +
                '}';
    }
}
