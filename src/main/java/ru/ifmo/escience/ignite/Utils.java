package ru.ifmo.escience.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.stream.StreamAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

public class Utils {
    private Utils() {
        // No-op.
    }

    public static void print(Object msg) {
        System.out.println(msg);
    }

    public static String readln() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            return br.readLine();
        }
    }

    public static void ensure(boolean condition, String msg) {
        if (!condition)
            throw new AssertionError(msg);
    }

    public static boolean sameAffinity(Ignite node, String firstCache, Object x, String secondCache, Object y) {
        Collection<ClusterNode> xaff = affinity(node, firstCache, x);

        Collection<ClusterNode> yaff = affinity(node, secondCache, y);

        return xaff.iterator().next().equals(yaff.iterator().next()) && xaff.equals(yaff);
    }

    private static Collection<ClusterNode> affinity(Ignite node, String cacheName, Object x) {
        return node.affinity(cacheName).mapKeyToPrimaryAndBackups(x);
    }
}
