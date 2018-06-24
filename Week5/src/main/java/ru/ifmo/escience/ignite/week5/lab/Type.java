package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.util.Objects;

public class Type {
    @QuerySqlField
    private final String description;

    @QuerySqlField(index = true)
    private final String type;

    public Type(String description, String type) {
        this.description = description;
        this.type = type;
    }

    public String getText() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type1 = (Type) o;
        return Objects.equals(description, type1.description) &&
                Objects.equals(type, type1.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, type);
    }

    @Override
    public String toString() {
        return "Type{" +
                "description='" + description + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
