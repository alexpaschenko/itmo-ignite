package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheKeyConfiguration;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.cache.affinity.AffinityKey;

import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.configuration.IgniteConfiguration;


import java.util.List;
import javax.cache.Cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import org.apache.ignite.lang.IgniteBiPredicate;

import java.sql.Time;
import java.util.Date;

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
//create
            //Cache broker
            CacheConfiguration<Long, Broker> brokerCacheConfiguration = new CacheConfiguration<>(brokerCacheName);
            //brokerCacheConfiguration.setIndexedTypes(Long.class, Broker.class);
            brokerCacheConfiguration.setCacheMode(CacheMode.REPLICATED);

            node.getOrCreateCache(brokerCacheConfiguration);

            //Cache client
            CacheConfiguration<AffinityKey<Long>, Client> clientCacheConfiguration = new CacheConfiguration<>(clientCacheName);
            clientCacheConfiguration.setIndexedTypes(AffinityKey.class, Client.class);
            clientCacheConfiguration.setCacheMode(CacheMode.PARTITIONED);

            IgniteConfiguration cfg = new IgniteConfiguration();
            cfg.setClientMode(true);
            cfg.setCacheConfiguration(clientCacheConfiguration);

            node.getOrCreateCache(clientCacheConfiguration);

            //Create ExchangeStock
            node.cache(clientCacheName).query(new SqlFieldsQuery("CREATE TABLE if not exists \"PUBLIC\".ExchangeStock" +
                    "(exchangeStock_id long, broker_id long, name varchar, country varchar," +
                    "primary key(exchangeStock_id, broker_id))" +
                    "WITH \"affinitykey=exchangeStock_id, cache_name=ExchangeStock, key_type=stockKey, value_type=exchangeStock\""));

            //Create ExchangeFutures
            node.cache(clientCacheName).query(new SqlFieldsQuery("CREATE TABLE if not exists \"PUBLIC\".ExchangeFutures" +
                    "(exchangeFutures_id long, broker_id long, name varchar, country varchar," +
                    "primary key(exchangeFutures_id, broker_id))" +
                    "WITH \"affinitykey=exchangeFutures_id, cache_name=ExchangeFutures, key_type=futuresKey, value_type=exchangeFutures\""));

//get and put
            // put Broker
            Broker broker1 = new Broker(1, 1, 1, "Finam");
            Broker broker2 = new Broker(2, 2, 2, "Alora");
            Broker broker3 = new Broker(1, 2, 2, "Finam");

            node.cache(brokerCacheName).put(broker1.getKey(), broker1);
            node.cache(brokerCacheName).put(broker2.getKey(), broker2);
            node.cache(brokerCacheName).put(broker3.getKey(), broker3);

            // view Broker
            Object brokerView = node.cache(brokerCacheName).get(broker2.getKey());
            System.out.println("// " + brokerView.toString() + " //");

            // put Client
            Client client1 = new Client(1, 1, "Nikita", "Ivanov",1000);
            Client client2 = new Client(2, 1, "Dmitriy", "Sergeev",10000);
            Client client3 = new Client(3, 1, "Vladimir", "Putin",100000);

            node.cache(clientCacheName).put(client1.getKey(), client1);
            node.cache(clientCacheName).put(client2.getKey(), client2);
            node.cache(clientCacheName).put(client3.getKey(), client3);

            // view Client
            Object clientView = node.cache(clientCacheName).get(client2.getKey());
            System.out.println("// " + clientView.toString() + " //");

// insert and join
            // insert and view ExchangeStock
            node.cache(clientCacheName).query(new SqlFieldsQuery(
                    "INSERT INTO \"PUBLIC\".ExchangeStock(exchangeStock_id, broker_id,  name, country) VALUES(?, ?, ?, ?)").
                    setArgs(1, 1, "MMVB", "Russia"));
            node.cache(clientCacheName).query(new SqlFieldsQuery(
                    "INSERT INTO \"PUBLIC\".ExchangeStock(exchangeStock_id, broker_id, name, country) VALUES(?, ?, ?, ?)").
                    setArgs(2, 1, "NYSE", "USA"));
            System.out.println("//" + node.cache(clientCacheName).query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\".ExchangeStock")).getAll() + "//");

            // insert and view ExchangeFutures
            node.cache(clientCacheName).query(new SqlFieldsQuery(
                    "INSERT INTO \"PUBLIC\".ExchangeFutures(exchangeFutures_id, broker_id,  name, country) VALUES(?, ?, ?, ?)").
                    setArgs(1, 1, "FORTS", "Russia"));
            node.cache(clientCacheName).query(new SqlFieldsQuery(
                    "INSERT INTO \"PUBLIC\".ExchangeFutures(exchangeFutures_id, broker_id, name, country) VALUES(?, ?, ?, ?)").
                    setArgs(2, 1, "CME", "USA"));
            node.cache(clientCacheName).query(new SqlFieldsQuery(
                    "INSERT INTO \"PUBLIC\".ExchangeFutures(exchangeFutures_id, broker_id, name, country) VALUES(?, ?, ?, ?)").
                    setArgs(3, 2, "NASDAQ", "USA"));
            System.out.println("//" + node.cache(clientCacheName).query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\".ExchangeFutures")).getAll() + "//");

            // join ExchangeStock and ExchangeFutures and print
            System.out.println("result of join: " + node.cache(clientCacheName).query(new SqlFieldsQuery("SELECT ex1.*, ex2.*  FROM \"PUBLIC\".ExchangeStock ex1 " +
                    "INNER JOIN \"PUBLIC\".ExchangeFutures ex2 on ex1.exchangeStock_id = ex2.exchangeFutures_id")).getAll());

        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
        finally
        {
            node.cache(clientCacheName).query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".ExchangeStock"));
            node.cache(clientCacheName).query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".ExchangeFutures"));
            node.close();
        }
    }
}