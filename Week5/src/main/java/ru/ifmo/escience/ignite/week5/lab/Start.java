package ru.ifmo.escience.ignite.week5.lab;

import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.cache.affinity.AffinityKey;

import java.sql.Time;
import java.util.Date;


public class Start {
    private static String ignitionConfig = "Week5/config/default-client.xml";
    private static String stockExchangeCacheName = "StockExchange";
    private static String financialInstrumentCacheName = "FinancialInstrument";
    private static String marketDepthCacheName = "MarketDepth";
    private static Ignite node;

    @SuppressWarnings("ThrowFromFinallyBlock")
    public static void main(String[] args) {
        node = Ignition.start(ignitionConfig);
        try {
            run();
        } catch (Exception e) {
            System.err.print(e.getMessage());
        } finally {
            dropAllAndClose();
        }
    }

    private static void run() {
        System.out.println(node.cacheNames());
        createStockExchange();
        createFinancialInstrument();
        createMarketDepth();
        viewJoinMarketDepthAndFinancialInstument();
        System.out.println(node.cacheNames());
    }

    private static void createStockExchange() {
        node.getOrCreateCache(stockExchangeCacheName);
        setStockExchangeCacheSettings();
        insertStockExchange();
        viewStockExchange();
    }

    private static void setStockExchangeCacheSettings() {
        CacheConfiguration<AffinityKey<Long>, StockExchange> colStockExchangeCfg =
                new CacheConfiguration<>(stockExchangeCacheName);
        colStockExchangeCfg.setCacheMode(CacheMode.PARTITIONED);
        colStockExchangeCfg.setIndexedTypes(AffinityKey.class, StockExchange.class);

        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        cfg.setCacheConfiguration(colStockExchangeCfg);
        node.getOrCreateCache(colStockExchangeCfg);
    }

    private static void insertStockExchange() {
        StockExchange exmo = new StockExchange(1, "exmo", "exmo.me");
        node.cache(stockExchangeCacheName).put(exmo.getKey(), exmo);
    }

    private static void viewStockExchange() {
        StockExchange exmo = new StockExchange(1, "exmo", "exmo.me");
        Object b = node.cache(stockExchangeCacheName).get(exmo.getKey());
        System.out.println(b.toString());
    }

    private static void createFinancialInstrument() {
        createFinancialInstrumentTable();
        insertFinancialInstruments();
        viewFinancialInstruments();
        System.out.println(node.cacheNames());
    }

    private static void createFinancialInstrumentTable() {
        String sql = "create table if not exists \"PUBLIC\"." + financialInstrumentCacheName + "\n" +
                "        (\n" +
                "        financial_instrument_id long,\n" +
                "        stock_exchange_id    long,\n" +
                "        name                 varchar,\n" +
                "        short_name           varchar,\n" +
                "        date_foundation      date,\n" +
                "        primary key (financial_instrument_id)\n" +
                "        ) " +
                "WITH \"affinitykey=FINANCIAL_INSTRUMENT_ID,cache_name=" + financialInstrumentCacheName + "," +
                "key_type=Long,value_type=financial_instrument\"";
        node.cache(stockExchangeCacheName)
                .query(new SqlFieldsQuery(sql))
                .getAll();
    }

    private static void insertFinancialInstruments() {
        System.out.println(node.cacheNames());
        node.cache(stockExchangeCacheName).query(new SqlFieldsQuery(
                "INSERT INTO \"PUBLIC\"." + financialInstrumentCacheName + "(financial_instrument_id, stock_exchange_id, name, short_name) " +
                        "VALUES(2, 1, 'Bitcoin', 'BTC')"));

        String sqlInsert = "INSERT INTO \"PUBLIC\"." + financialInstrumentCacheName + "(financial_instrument_id, stock_exchange_id, name, short_name, date_foundation) VALUES(?, ?, ?, ?, ?)";
        SqlFieldsQuery a = new SqlFieldsQuery(sqlInsert).setArgs(1, 1, "USD", "USD", new Date());
        node.cache(stockExchangeCacheName)
                .query(a);
    }

    private static void viewFinancialInstruments() {
        System.out.println(node.cache(stockExchangeCacheName)
                .query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\"." + financialInstrumentCacheName))
                .getAll());
    }

    private static void createMarketDepth() {
        createMarketDepthTable();
        insertMarketDepths();
        viewMarketDepths();
    }

    private static void createMarketDepthTable() {
        String sql = "create table if not exists \"PUBLIC\"." + marketDepthCacheName + "\n" +
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
                "WITH \"affinitykey=MARKET_DEPTH_ID,cache_name=" + marketDepthCacheName + "," +
                "key_type=MarketDepthKey,value_type=MarketDepth\"";
        node.cache(stockExchangeCacheName).query(new SqlFieldsQuery(sql)).getAll();
    }

    private static void insertMarketDepths() {
        MarketDepth a = new MarketDepth(1, 1, 1, 2, new Date(), new Time(1, 1, 1), 1, 1.25, 1, 1.25, "sell");
        node.cache(marketDepthCacheName).put(a.getKey(), a);
    }

    private static void viewMarketDepths() {
        MarketDepth a = new MarketDepth(1, 1, 1, 2, new Date(), new Time(1, 1, 1), 1, 1.25, 1, 1.25, "sell");

        Object b = node.cache(marketDepthCacheName).get(a.getKey());
        System.out.println(b.toString());
        System.out.println(node.cache(marketDepthCacheName)
                .query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\"." + marketDepthCacheName))
                .getAll());
    }

    private static void viewJoinMarketDepthAndFinancialInstument() {
        String marketDepthTable = "\"PUBLIC\"." + marketDepthCacheName;
        String financialInstrumentsTable = "\"PUBLIC\"." + financialInstrumentCacheName;
        String sql = "SELECT md.*, instrument.*  FROM " + marketDepthTable + " md INNER JOIN " + financialInstrumentsTable + " instrument on md.financial_instrument_in = instrument.financial_instrument_id";
        List<List<?>> res = node.cache(stockExchangeCacheName).query(new SqlFieldsQuery(sql)).getAll();
        System.out.println(node.cache(stockExchangeCacheName).query(new SqlFieldsQuery(sql)).getAll());
        System.out.println("Query results join:");

        for (Object next : res)
            System.out.println(">>>    " + next);
    }

    private static void dropAllAndClose() {
        System.out.println(node.cacheNames());
        createOrGetAllCaches();
        dropAllTables();
        node.destroyCaches(node.cacheNames());
        System.out.println("destroyCaches" + node.cacheNames());
        node.close();
    }

    private static void createOrGetAllCaches() {
        node.getOrCreateCache(financialInstrumentCacheName);
        node.getOrCreateCache(stockExchangeCacheName);
        node.getOrCreateCache(marketDepthCacheName);
    }

    private static void dropAllTables() {
        node.cache(stockExchangeCacheName).query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\"." + financialInstrumentCacheName));
        node.cache(stockExchangeCacheName).query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\"." + stockExchangeCacheName));
        node.cache(stockExchangeCacheName).query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\"." + marketDepthCacheName));
    }
}