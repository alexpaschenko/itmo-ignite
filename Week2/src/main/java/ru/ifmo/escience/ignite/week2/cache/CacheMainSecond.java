package ru.ifmo.escience.ignite.week2.cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

import static ru.ifmo.escience.ignite.Utils.print;

public class CacheMainSecond {
    public static void main(String[] args) throws Exception {
        try (Ignite ignite = Ignition.start("Week2/config/default.xml")) {
            IgniteCache<Object, Object> cache = ignite.cache("mycache");

            print("Waiting for data...");

            while (cache.get("START") == null)
                Thread.sleep(500);
        }
    }
}
