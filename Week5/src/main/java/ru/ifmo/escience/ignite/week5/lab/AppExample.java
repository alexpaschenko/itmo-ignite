package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import ru.ifmo.escience.ignite.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ru.ifmo.escience.ignite.Utils.print;

public class AppExample {
    public static void main(String[] args) {
        CacheConfiguration<String, User> userCacheConfig = new CacheConfiguration<>("User");
        userCacheConfig.setCacheMode(CacheMode.PARTITIONED);
        userCacheConfig.setIndexedTypes(String.class, User.class);
        CacheConfiguration<Integer, Entry> entryCacheConfig = new CacheConfiguration<>(Entry.class.getName());
        entryCacheConfig.setIndexedTypes(Integer.class, Entry.class);
        CacheConfiguration<Integer, Type> typeCacheConfig = new CacheConfiguration<>("Type");
        typeCacheConfig.setCacheMode(CacheMode.REPLICATED);

        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        cfg.setCacheConfiguration(userCacheConfig);
        Ignite node = Ignition.start("Week5/config/default-client.xml");
        node.getOrCreateCache(userCacheConfig);
        node.getOrCreateCache(entryCacheConfig);
        node.getOrCreateCache(typeCacheConfig);

        try {
            node.cache("User").query(new SqlFieldsQuery("CREATE TABLE if not exists \"PUBLIC\".Followers(id int, user_login varchar, " +
                    "follower_login varchar, primary key(id, user_login)) WITH \"affinitykey=USER_LOGIN, cache_name=Followers," +
                    "key_type=FollowersKey,value_type=Followers\""));
            node.cache("User").query(new SqlFieldsQuery("CREATE TABLE if not exists \"PUBLIC\".Media(id int, entry_id int, " +
                    "link varchar, primary key(id, entry_id)) WITH \"affinitykey=ENTRY_ID, cache_name=Media," +
                    "key_type=MediaKey,value_type=Media\""));

            node.cache("User").putAll(generateUsers());
            node.cache(Entry.class.getName()).putAll(generateEntries());
            insertMedia(node);

            System.out.println(node.cache("User").get("vanillacoder"));
            System.out.println(node.cache("User")
                    .query(new SqlFieldsQuery("SELECT * FROM USER WHERE NAME = 'SpaceDoshik'"))
                    .getAll());
            System.out.println(node.cacheNames());
            System.out.println(node.cache("User")
                    .query(new SqlFieldsQuery("SELECT LINK FROM \"PUBLIC\".MEDIA MEDIA" +
                            " INNER JOIN \"ru.ifmo.escience.ignite.week5.lab.Entry\".ENTRY ENTRY ON MEDIA.ENTRY_ID=ENTRY.ID" +
                            " INNER JOIN USER ON ENTRY.LOGIN=USER.LOGIN" +
                            " WHERE NAME = 'SpaceDoshik'"))
                    .getAll());
        } finally {
            node.cache("User").query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".Followers"));
            node.cache("User").query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".Media"));
            node.destroyCaches(Arrays.asList(Entry.class.getName(), "User", "Type"));
            node.close();
        }
    }

    private static void insertMedia(Ignite node) {
        for (int i = 0; i < 5; i++) {
            node.cache("Media").query(new SqlFieldsQuery("insert into \"PUBLIC\".Media(id, entry_id, link) " +
                    "values(?, ?, ?)").setArgs(i, i, "example.com/" + i));
        }
    }

    private static Map<String, User> generateUsers() {
        Map<String, User> users = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            User u = new User("u" + String.valueOf(i), String.valueOf(i), "regular");
            users.put(String.valueOf(i), u);
        }
        users.put("vanillacoder", new User("SpaceDoshik", "vanillacoder", "admin"));
        return users;
    }

    private static Map<Integer, Entry> generateEntries() {
        Map<Integer, Entry> entries = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            Entry e = new Entry(i, "text" + i, "vanillacoder");
            entries.put(i, e);
        }
        for (int i = 0; i < 5; i++) {
            Entry e = new Entry(i + 5, "other" + i, String.valueOf(i));
            entries.put(i + 5, e);
        }
        return entries;
    }
}
