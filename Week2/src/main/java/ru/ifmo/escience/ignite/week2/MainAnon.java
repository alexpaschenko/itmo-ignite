package ru.ifmo.escience.ignite.week2;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;

public class MainAnon {
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start()) {
            String res = ignite.compute().call(new IgniteCallable<String>() {
                public String call() {
                    return "*** Hello World ***";
                }
            });

            System.out.println(res);
        }
    }
}
