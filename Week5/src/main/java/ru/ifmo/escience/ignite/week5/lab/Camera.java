package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.affinity.AffinityKeyMapped;

import java.io.Serializable;

public class Camera implements Serializable {
    @QuerySqlField(index = true)
    private final int id;
    @QuerySqlField
    private final int city;


    public Camera(int id, int city) {
        this.id = id;
        this.city = city;
    }


    @Override
    public String toString() {
        return "Camera{" +
                "id=" + id +
                ", city='" + city + '\'' +
                '}';
    }
}
