package ru.ifmo.escience.ignite.week2;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteFuture;

public class Main {
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start()) {
            IgniteFuture<String> res = ignite.compute().callAsync(new HelloWorldCallable());

            System.out.println(res.get());
        }
    }
}
