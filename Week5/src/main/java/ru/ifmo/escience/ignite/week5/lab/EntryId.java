package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.util.Objects;

public class EntryId {
    @QuerySqlField
    private final int id;
    @QuerySqlField
    @AffinityKeyMapped
    private final String login;

    public EntryId(int id, String login) {
        this.id = id;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntryId entryId = (EntryId) o;
        return id == entryId.id &&
                Objects.equals(login, entryId.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }

    @Override
    public String toString() {
        return "EntryId{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }
}


