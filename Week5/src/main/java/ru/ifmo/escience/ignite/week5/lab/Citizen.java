package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.affinity.AffinityKeyMapped;

import java.io.Serializable;

public class Citizen implements Serializable {
    @QuerySqlField(index = true)
    private final int id;
    @QuerySqlField
    private final String name;
    @QuerySqlField
    @AffinityKeyMapped
    private final int city;


    public Citizen(int id, String name, int city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    @Override
    public String toString() {
        return "Citizen{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city=" + city +
                '}';
    }
}

