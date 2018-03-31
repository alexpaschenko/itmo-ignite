package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.internal.util.typedef.internal.S;

public class Entry {
    @QuerySqlField(index = true)
    private final Integer id;

    @QuerySqlField(index = true)
    @AffinityKeyMapped
    private final String text;

    public Entry(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return S.toString(Entry.class, this);
    }
}
