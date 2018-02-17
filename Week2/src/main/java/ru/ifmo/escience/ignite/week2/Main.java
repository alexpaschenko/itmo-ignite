package ru.ifmo.escience.ignite.week2;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

public class Main {
    public static void main(String[] args) {
        try (Ignite ignite = Ignition.start()) {
            String res = ignite.compute().call(new HelloWorldCallable());

            System.out.println(res);
        }
    }
}
