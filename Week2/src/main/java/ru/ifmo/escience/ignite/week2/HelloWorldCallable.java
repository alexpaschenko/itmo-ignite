package ru.ifmo.escience.ignite.week2;

import org.apache.ignite.lang.IgniteCallable;

public class HelloWorldCallable implements IgniteCallable<String> {
    public String call() {
        return "*** Hello World ***";
    }
}
