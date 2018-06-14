package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import ru.ifmo.escience.ignite.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import static ru.ifmo.escience.ignite.Utils.print;
import static ru.ifmo.escience.ignite.Utils.readln;


public class SpySession {
    @SuppressWarnings("ThrowFromFinallyBlock")
    public static void main(String[] args) throws IOException {

        Ignite node1 = Ignition.start("Week5/config/default-client.xml");

        try {
            IgniteCache cache = node1.getOrCreateCache(
                    new CacheConfiguration<>()
                            .setName("Citizen")
                            .setIndexedTypes(int.class, Citizen.class));
            Citizen c = new Citizen(1, "Vasya", 1);
            node1.cache("Citizen").put(1, c);
//            cache.put(2, new Citizen(2,"Vasya", 2));
//            cache.put(3, new Citizen(3,"Petya", 1));
//            cache.put(4, new Citizen(4,"Sasha", 3));
//            cache.put(5, new Citizen(5,"Vova", 2));
//            cache.put(6, new Citizen(6,"Yura", 2));

            cache = node1.getOrCreateCache(
                    new CacheConfiguration<>()
                            .setName("City")
                            .setIndexedTypes(Long.class, City.class));

            cache.put(1, new City(1, "Spb"));
            cache.put(2, new City(2, "Moscow"));
            cache.put(3, new City(3, "RodnoyMuhosransk"));



            cache = node1.getOrCreateCache(
                    new CacheConfiguration<>()
                            .setName("Camera")
                            .setIndexedTypes(Long.class, Camera.class));

            for (int i = 0; i <= 5; i++) {
                cache.put(i, new Camera(i, 1));
                cache.put(i, new Camera(i, 2));
            }

            for (int i = 6; i <= 10; i++) {
                cache.put(i, new Camera(i, 2));
            }

            cache.put(1488, new Camera(11, 3));


            cache.query(new SqlFieldsQuery("CREATE TABLE IF NOT EXISTS RECORDINGS " +
                    "(date Date, cityId int primary key, cameraId int, citizenId int)").setSchema("PUBLIC")).getAll();

            print(node1.cacheNames());

        }
        finally {
            node1.destroyCaches(Arrays.asList("Citizen", "Camera", "City"));
            node1.close();
        }
    }
}
