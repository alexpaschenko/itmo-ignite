package ru.ifmo.escience.ignite.week4;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.internal.util.typedef.internal.S;

public class Person {
    @QuerySqlField(index = true, groups = {"fullname"})
    private final String name;

    @QuerySqlField(index = true, groups = {"surname", "fullname"})
    private final String surname;

    @QuerySqlField(index = true)
    private final String login;

    @QuerySqlField(index = true)
    private final String instagramLogin;




    public Person(String name, String surname, String login, String instagramLogin) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.instagramLogin = instagramLogin;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLogin() {
        return login;
    }

    public String getInstagramLogin() {
        return instagramLogin;
    }

    @Override
    public String toString() {
        return S.toString(Person.class, this);
    }
}
