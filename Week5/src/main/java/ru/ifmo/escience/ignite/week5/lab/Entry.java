package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.internal.util.typedef.internal.S;

public class Entry {
    @QuerySqlField(index = true)
    private final Integer id;

    @QuerySqlField
    private final String text;

    @QuerySqlField
    @AffinityKeyMapped
    private final String login;

    public Entry(Integer id, String text, String login) {
        this.id = id;
        this.text = text;
        this.login = login;
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return S.toString(Entry.class, this);
    }

}
