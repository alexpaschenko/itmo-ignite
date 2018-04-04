package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.internal.util.typedef.internal.S;

public class User {
    @QuerySqlField
    private final String name;

    @QuerySqlField(index = true)
    private final String login;

    @AffinityKeyMapped
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

    @Override
    public String toString() {
        return S.toString(User.class, this);
    }

    public String getUserType() {
        return userType;
    }
}
