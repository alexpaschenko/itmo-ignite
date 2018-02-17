package ru.ifmo.escience.ignite.week2.cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CachePeekMode;

import static ru.ifmo.escience.ignite.Utils.print;

public class CacheUtils {
    public static void printCacheStats(Ignite ignite) {
        IgniteCache<Object, Object> cache = ignite.cache("mycache");

        cache.remove("START");

        cache.remove("FINISH");

        print("Primary entries: " + cache.localSize(CachePeekMode.PRIMARY));

        /* SET YOUR RANDOM NUMBER HERE */
        int cnt = 0;

        /* PUT DATA HERE */
        for (int i = 0; i < cnt; i++)
            ;

        int sum = sumFromCache(cache);

        /* PUT SUM INTO CACHE WITH KEY 'FINISH' HERE */

        printCacheStats(ignite);
    }

    private static int sumFromCache(IgniteCache<Object, Object> cache) {
        return 0;
    }
}
