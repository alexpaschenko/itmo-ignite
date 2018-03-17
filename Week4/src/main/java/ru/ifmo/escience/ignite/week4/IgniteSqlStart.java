package ru.ifmo.escience.ignite.week4;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import java.io.IOException;

import static ru.ifmo.escience.ignite.Utils.readln;

public class IgniteSqlStart {
    public static void main(String[] args) throws IOException {
        System.setProperty("IGNITE_H2_DEBUG_CONSOLE", "true");

        try (Ignite node = Ignition.start("Week4/config/default.xml")) {
            node.cache("mycache").put(1, new Person("John", "Doe", 5000));



            readln();
        }
    }
}
