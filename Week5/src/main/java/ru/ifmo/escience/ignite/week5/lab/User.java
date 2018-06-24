package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.internal.util.typedef.internal.S;

import java.util.Objects;

public class User {
    @QuerySqlField
    private final String name;

    @QuerySqlField(index = true)
    private final String login;

    @QuerySqlField
    private final String userType;


    public User(String name, String login, String userType) {
        this.name = name;
        this.login = login;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getUserType() {
        return userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(login, user.login) &&
                Objects.equals(userType, user.userType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, login, userType);
    }

    @Override
    public String toString() {
        return S.toString(User.class, this);
    }
}
