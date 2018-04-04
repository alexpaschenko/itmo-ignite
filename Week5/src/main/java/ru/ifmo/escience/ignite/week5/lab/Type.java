package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

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
    public String toString() {
        return "Type{" +
                "description='" + description + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
