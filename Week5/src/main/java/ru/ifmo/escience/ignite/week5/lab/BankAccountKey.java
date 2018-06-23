package ru.ifmo.escience.ignite.week5.lab;

/**
 * Let's assume this class is really unreachable.
 * Nobody can put annotations here and this class is very important of course.
 * NB: TBH idea of having separate key class from unreachable class looks pretty unlikely for me.
 * I hope I just don't know proper way to deal with classes without separate key
 */
public class BankAccountKey {
    private final int id;
    private final int bankId;

    public BankAccountKey(int id, int bankId) {
        this.id = id;
        this.bankId = bankId;
    }

    public int getId() {
        return id;
    }

    public int getBankId() {
        return bankId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankAccountKey that = (BankAccountKey) o;

        if (id != that.id) return false;
        return bankId == that.bankId;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + bankId;
        return result;
    }

    @Override
    public String toString() {
        return "BankAccountKey{" +
                "id=" + id +
                ", bankId=" + bankId +
                '}';
    }
}
