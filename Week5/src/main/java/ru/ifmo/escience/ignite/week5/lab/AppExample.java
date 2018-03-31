package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheKeyConfiguration;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import java.util.HashMap;
import java.util.Map;

public class AppExample {
    public static void main(String[] args) {
        CacheConfiguration<Integer, User> userCacheConfig = new CacheConfiguration<>("User");
        userCacheConfig.setIndexedTypes(Integer.class, User.class);
        CacheConfiguration<Integer, Entry> entryCacheConfig = new CacheConfiguration<>("Entry");
        CacheConfiguration<Integer, Type> typeCacheConfig = new CacheConfiguration<>("Type");

        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        cfg.setCacheConfiguration(userCacheConfig);
        Ignite node1 = Ignition.start("Week5/config/default-client.xml");
        node1.getOrCreateCache(userCacheConfig);
        node1.getOrCreateCache(entryCacheConfig);
        node1.getOrCreateCache(typeCacheConfig);

        try {
            node1.cache("User").query(new SqlFieldsQuery("CREATE TABLE if not exists \"PUBLIC\".Followers(id int, user_login varchar, " +
                    "follower_login varchar, primary key(id, user_login)) WITH \"affinitykey=USER_LOGIN, cache_name=Followers," +
                    "key_type=FollowersKey,value_type=Followers\""));
            node1.cache("User").query(new SqlFieldsQuery("CREATE TABLE if not exists \"PUBLIC\".Media(id int, user_login varchar, " +
                    "link varchar, primary key(id, user_login)) WITH \"affinitykey=USER_LOGIN, cache_name=Media," +
                    "key_type=MediaKey,value_type=Media\""));

            node1.cache("User").putAll(generateUsers());
            node1.cache("ru.ifmo.escience.ignite.week5.lab.Entry").putAll(generateEntries());
            node1.cache("Media").query(new SqlFieldsQuery("insert into \"PUBLIC\".Media(id, user_login, link) " +
                    "values(1, 'u1', 'example.com/1')"));

        } finally {
            node1.close();
        }
    }

    private static Map<Integer, User> generateUsers() {
        Map users = new HashMap();
        for (int i = 0; i < 5; i++) {
            User u = new User("u" + String.valueOf(i), String.valueOf(i), "regular");
            users.put(i, u);
        }
        return users;
    }

    private static Map<Integer, User> generateEntries() {
        Map entries = new HashMap();
        for (int i = 0; i < 5; i++) {
            Entry u = new Entry(i, "text");
            entries.put(i, u);
        }
        return entries;
    }
}
