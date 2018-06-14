package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class City {
    @QuerySqlField(index = true)
    private final int id;
    @QuerySqlField
    private final String name;


    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "City{" +
                "Id=" + id +
                ", Name =" + name +
                '}';
    }
}