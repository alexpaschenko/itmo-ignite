package ru.ifmo.escience.ignite.week2.cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

import static ru.ifmo.escience.ignite.Utils.print;
import static ru.ifmo.escience.ignite.Utils.readln;
import static ru.ifmo.escience.ignite.week2.cache.CacheUtils.TOTAL;
import static ru.ifmo.escience.ignite.week2.cache.CacheUtils.printCacheStats;

public class CacheMainFirst {
    public static void main(String[] args) throws Exception {
        try (Ignite ignite = Ignition.start("Week2/config/default.xml")) {
            int cnt = randomNumber();

            int sum = calculateExpectedSum(cnt);

            print("Random number is: " + cnt);
            print("Another node must put this number of items: " + (TOTAL - cnt));

            IgniteCache<Object, Object> cache = ignite.cache("mycache");

            for (int i = 1; i <= cnt; i++)
                cache.put(i, new Person(i,i * 2));

            cache.put("START", true);

            print("Waiting for result...");

            Object done;

            while ((done = cache.get("FINISH")) == null)
                Thread.sleep(500);

            assert cache.size() == TOTAL : ("Number of records must be " + TOTAL);

            checkResult(done, sum);

            printCacheStats(ignite);

            readln();
        }
    }

    private static void checkResult(Object done, int sum) {
        assert done instanceof Integer : "Invalid result type: " + done.getClass().getName();

        assert (int)done == sum : "Invalid result: " + done;
    }

    private static int randomNumber() {
        return 1 + (int)(Math.random() * (TOTAL - 1));
    }

    private static int calculateExpectedSum(int cnt) {
        int res = 0;

        for (int i = 1; i <= cnt; i++)
            res += cnt * 2;

        for (int i = cnt + 1; i <= TOTAL; i++)
            res += cnt * 3;

        return res;
    }
}
