package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheKeyConfiguration;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;


public class Run {
    private static String ignitionConfig = "Week5/config/default-client.xml";
    private static String exchangeCacheName = "Exchange";
    private static String brokerCacheName = "Broker";
    private static String clientCacheName = "Client";
    private static Ignite node;

    public static void main(String[] args) {
        node = Ignition.start(ignitionConfig);
        try
        {
            CacheConfiguration<Long, Broker> brokerCacheConfiguration = new CacheConfiguration<>(brokerCacheName);
            brokerCacheConfiguration.setIndexedTypes(Integer.class, Broker.class);
            brokerCacheConfiguration.setCacheMode(CacheMode.REPLICATED);

            CacheConfiguration<Long, Client> clientCacheConfiguration = new CacheConfiguration<>(clientCacheName);

            node.getOrCreateCache(brokerCacheConfiguration);
            node.getOrCreateCache(clientCacheConfiguration);


            node.cache(exchangeCacheName).query(new SqlFieldsQuery("CREATE TABLE if not exists \"PUBLIC\".exchange" +
                    "(exchange_id long, name varchar, country varchar, type varchar, primary key(exchange_id)) " +
                    "WITH \"affinitykey=EXCHANGE_ID,cache_name=Exchange," +
                    "key_type=ExchangeKey,value_type=exchange\"")).getAll();


            CacheKeyConfiguration clientConf = new CacheKeyConfiguration(Client.class);
            node.configuration().setCacheKeyConfiguration(clientConf);

//insert
            node.cache(exchangeCacheName).query(new SqlFieldsQuery(
                    "INSERT INTO \"PUBLIC\".exchange(exchange_id, name, country, type) VALUES(?, ?, ?, ?)").
                    setArgs(1, "FORTS", "Russia", "Derivatives_exchange"));
            node.cache(exchangeCacheName).query(new SqlFieldsQuery(
                    "INSERT INTO \"PUBLIC\".exchange(exchange_id, name, country, type) VALUES(?, ?, ?, ?)").
                    setArgs(2, "NYSE", "USA", "Stock_exchange" ));


            Broker br = new Broker(1, 1, 1, "Finam");
            node.cache(brokerCacheName).put(1, br);


            node.binary().builder("ClientKey")
                    .setField("CLIENT_ID", 1)
                    .setField("BROKER_ID", 1)
                    .setField("FIRST_NAME", "Nikita")
                    .setField("SECOND_NAME", "Ivanov")
                    .setField("AMOUNT", 100000)
                    .build();

            node.binary().builder("ClientKey")
                    .setField("CLIENT_ID", 2)
                    .setField("BROKER_ID", 2)
                    .setField("FIRST_NAME", "Dmitriy")
                    .setField("SECOND_NAME", "Sergeev")
                    .setField("AMOUNT", 1000000)
                    .build();

//print
            System.out.println(node.cache(exchangeCacheName).query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\".exchange")).getAll());
        }
        catch (Exception ex)
        {
            System.out.println("ex");
        }
        finally
        {
            node.cache(exchangeCacheName).query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".exchange"));

            node.close();
        }
    }
}
