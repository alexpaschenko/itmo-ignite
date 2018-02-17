package ru.ifmo.escience.ignite.week2.cluster;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import static ru.ifmo.escience.ignite.Utils.print;
import static ru.ifmo.escience.ignite.Utils.readln;

public class ClusterMain {
    public static void main(String[] args) throws Exception {
        try (Ignite ignite = Ignition.start("Week2/config/default.xml")) {
            Thread.sleep(500);

            print("Waiting for another node to connect...");

            while (ignite.cluster().nodes().size() < 2) {
                Thread.sleep(500);
            }

            print("Another node found!");
            print("Press <Enter> to exit...");

            readln();
        }
    }
}
