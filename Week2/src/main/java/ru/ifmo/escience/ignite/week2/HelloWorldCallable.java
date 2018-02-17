package ru.ifmo.escience.ignite.week2;

import org.apache.ignite.lang.IgniteCallable;

public class HelloWorldCallable implements IgniteCallable<String> {
    public String call() throws Exception {
        Thread.sleep(5000);

        return "*** Hello World ***";
    }
}
