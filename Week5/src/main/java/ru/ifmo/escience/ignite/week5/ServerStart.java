package ru.ifmo.escience.ignite.week5;

import org.apache.ignite.Ignition;

public class ServerStart {
    public static void main(String[] args) {
        Ignition.start("Week5/config/default.xml");
    }
}
