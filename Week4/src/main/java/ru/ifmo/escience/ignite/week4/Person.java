package ru.ifmo.escience.ignite.week4;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.internal.util.typedef.internal.S;

public class Person {
    @QuerySqlField(index = true, groups = {"fullname"})
    private final String name;

    @QuerySqlField(index = true, groups = {"surname", "fullname"})
    private final String surname;

    @QuerySqlField(index = true)
    private final int salary;

    public Person(String name, String surname, int salary) {
        this.name = name;
        this.surname = surname;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return S.toString(Person.class, this);
    }
}
