package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CacheKeyConfiguration;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import java.sql.Time;
import java.util.Date;

public class Start {
    private static String ignitionConfig = "Week5/config/default-client.xml";
    private static String stockExchangeCacheName = "StockExchange";
    private static String financialInstrumentCacheName = "FinancialInstrument";
    private static String marketDepthCacheName = "MarketDepth";
    private static Ignite node;

    public static void main(String[] args) {
        node = Ignition.start(ignitionConfig);
        try {
            run();
        } finally {
            dropAllTables();
            node.close();
        }
    }

    private static void run() {
        createCaches();
        createTablesBySql();
        setCacheKeyConfiguration();
        insertDataInCaches();
        showAllRecords();
        System.out.println(node.cacheNames());
    }

    private static void createCaches() {
        node.getOrCreateCache(getStockExchangeCacheConfiguration());
        node.getOrCreateCache(getFinancialInstrumentCacheConfiguration());
        node.getOrCreateCache(getMarketDepthCacheConfiguration());
    }

    private static CacheConfiguration<Long, StockExchange> getStockExchangeCacheConfiguration() {
        CacheConfiguration<Long, StockExchange> stockExchange = new CacheConfiguration<>(stockExchangeCacheName);
        stockExchange.setIndexedTypes(Integer.class, StockExchange.class);
        stockExchange.setCacheMode(CacheMode.REPLICATED);
        return stockExchange;
    }

    private static CacheConfiguration<Long, FinancialInstrument> getFinancialInstrumentCacheConfiguration() {
        return new CacheConfiguration<>(financialInstrumentCacheName);
    }

    private static CacheConfiguration<Long, MarketDepth> getMarketDepthCacheConfiguration() {
        return new CacheConfiguration<>(marketDepthCacheName);
    }

    private static void createTablesBySql() {
        createStockExchangeTable();
        createFinancialInstrumentTable();
        createMarketDepthTable();
    }

    private static void createStockExchangeTable() {
        String sql = "CREATE TABLE if not exists \"PUBLIC\".stock_exchange" +
                "(" +
                "stock_exchange_id long, " +
                "name varchar, " +
                "primary_domain varchar, " +
                "primary key(stock_exchange_id)) " +
                "WITH \"affinitykey=STOCK_EXCHANGE_ID,cache_name=stock_exchange," +
                "key_type=StockExchangeKey,value_type=stock_exchange\"";
        node.cache(stockExchangeCacheName).query(new SqlFieldsQuery(sql)).getAll();
    }

    private static void createFinancialInstrumentTable() {
        String sql = "create table if not exists \"PUBLIC\".financial_instrument\n" +
                "        (\n" +
                "        financial_instrument_id long,\n" +
                "        stock_exchange_id    long,\n" +
                "        name                 varchar,\n" +
                "        short_name           varchar,\n" +
                "        date_foundation      date,\n" +
                "        primary key (financial_instrument_id)\n" +
                "        ) " +
                "WITH \"affinitykey=FINANCIAL_INSTRUMENT_ID,cache_name=financial_instrument," +
                "key_type=FinancialInstrumentKey,value_type=financial_instrument\"";
        node.cache(financialInstrumentCacheName).query(new SqlFieldsQuery(sql)).getAll();
    }

    private static void createMarketDepthTable() {
        String sql = "create table if not exists \"PUBLIC\".market_depth\n" +
                "        (\n" +
                "                market_depth_id                   long,\n" +
                "                stock_exchange_id    long ,\n" +
                "                financial_instrument_in long ,\n" +
                "                financial_instrument_out long ,\n" +
                "                order_date           DATE  ,\n" +
                "                order_time           TIME  ,\n" +
                "                financial_instrument_in_count decimal   ,\n" +
                "                financial_instrument_out_count decimal  ,\n" +
                "                reduced_price_in     decimal   ,\n" +
                "                reduced_price_out    decimal   ,\n" +
                "                direction            varchar   ,\n" +
                "        primary key (market_depth_id)\n" +
                "        )" +
                "WITH \"affinitykey=MARKET_DEPTH_ID,cache_name=market_depth," +
                "key_type=MarketDepthKey,value_type=market_depth\"";
        node.cache(marketDepthCacheName).query(new SqlFieldsQuery(sql)).getAll();
    }

    private static void setCacheKeyConfiguration() {
        CacheKeyConfiguration marketDepthConf = new CacheKeyConfiguration(MarketDepth.class);
        node.configuration().setCacheKeyConfiguration(marketDepthConf);
    }

    private static void insertDataInCaches() {
        insertStockExchanges();
        insertFinancialInstruments();
        insertMarketDepths();
    }

    private static void insertStockExchanges() {
        node.binary().builder("StockExchangeKey")
                .setField("STOCK_EXCHANGE_ID", 1)
                .setField("NAME", "exmo")
                .setField("PRIMARY_DOMAIN ", "exmo.me")
                .build();

        node.binary().builder("StockExchangeKey")
                .setField("STOCK_EXCHANGE_ID", 2)
                .setField("NAME", "Bitfenix")
                .setField("PRIMARY_DOMAIN ", "bitfenix.com")
                .build();

        node.binary().builder("StockExchangeKey")
                .setField("STOCK_EXCHANGE_ID", 3)
                .setField("NAME", "Bitstamp")
                .setField("PRIMARY_DOMAIN ", "bitstamp.com")
                .build();
    }

    private static void insertFinancialInstruments() {
        node.cache(financialInstrumentCacheName).query(new SqlFieldsQuery(
                "INSERT INTO \"PUBLIC\".financial_instrument(financial_instrument_id, name, short_name, date_foundation) VALUES(?, ?, ?, ?)").
                setArgs(1, "USD", "USD", new Date()));
        node.cache(financialInstrumentCacheName).query(new SqlFieldsQuery(
                "INSERT INTO \"PUBLIC\".financial_instrument(financial_instrument_id, name, short_name, date_foundation) VALUES(?, ?, ?, ?)").
                setArgs(2, "Bitcoin", "BTC", new Date()));
    }

    private static void insertMarketDepths() {
        MarketDepth a = new MarketDepth(1, 1, 1, 2, new Date(), new Time(1, 1, 1), 1, 1.25, 1, 1.25, "sell");
        node.cache(marketDepthCacheName).put(1, a);
    }

    private static void showAllRecords() {
        System.out.println(node.cache(stockExchangeCacheName).query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\".stock_exchange")).getAll());
        System.out.println(node.cache(marketDepthCacheName).query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\".market_depth")).getAll());
        System.out.println(node.cache(financialInstrumentCacheName).query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\".financial_instrument")).getAll());
    }

    private static void dropAllTables() {
        node.cache(financialInstrumentCacheName).query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".financial_instrument"));
        node.cache(stockExchangeCacheName).query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".stock_exchange"));
        node.cache(marketDepthCacheName).query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".market_depth"));
    }

    private static void run2() {
        node.getOrCreateCache(financialInstrumentCacheName);

        String sql = "create table if not exists \"PUBLIC\".financial_instrument\n" +
                "        (\n" +
                "        financial_instrument_id long,\n" +
                "        stock_exchange_id    long,\n" +
                "        name                 varchar,\n" +
                "        short_name           varchar,\n" +
                "        date_foundation      date,\n" +
                "        primary key (financial_instrument_id)\n" +
                "        ) " +
                "WITH \"affinitykey=FINANCIAL_INSTRUMENT_ID,cache_name=financial_instrument," +
                "key_type=FinancialInstrumentKey,value_type=FinancialInstrument\"";
        node.cache(financialInstrumentCacheName).query(new SqlFieldsQuery(sql)).getAll();

        node.cache(financialInstrumentCacheName).query(new SqlFieldsQuery(
                "INSERT INTO \"PUBLIC\".financial_instrument(financial_instrument_id, name, short_name, date_foundation) VALUES(?, ?, ?, ?)").
                setArgs(2, "Bitcoin", "BTC", new Date()));

        System.out.println(node.cache(financialInstrumentCacheName).query(new SqlFieldsQuery(
                "SELECT * FROM \"PUBLIC\".financial_instrument")).getAll());

        node.cache(financialInstrumentCacheName).query(new SqlFieldsQuery(
                "DROP TABLE IF EXISTS \"PUBLIC\".financial_instrument"));

        System.out.println(node.cacheNames());
    }
}