package ru.ifmo.escience.ignite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {
    private Utils() {
        // No-op.
    }

    public static void print(String msg) {
        System.out.println(msg);
    }

    public static String readln() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            return br.readLine();
        }
    }
}
