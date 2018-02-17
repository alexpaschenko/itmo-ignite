package ru.ifmo.escience.ignite.week2.cache;

public class Person {
    private final int id;

    private final int salary;

    public Person(int id, int salary) {
        this.id = id;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public int getSalary() {
        return salary;
    }
}
