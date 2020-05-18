package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.internal.util.typedef.internal.S;

import java.util.Objects;

public class Entry {
    @QuerySqlField(index = true)
    private final EntryId entryId;

    @QuerySqlField
    private final String text;


    public Entry(EntryId entryId, String text) {
        this.entryId = entryId;
        this.text = text;
    }


    public EntryId getEntryId() {
        return entryId;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return Objects.equals(entryId, entry.entryId) &&
                Objects.equals(text, entry.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entryId, text);
    }

    @Override
    public String toString() {
        return S.toString(Entry.class, this);
    }

}
