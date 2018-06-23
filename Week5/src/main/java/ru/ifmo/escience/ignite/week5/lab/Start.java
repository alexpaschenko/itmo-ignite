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
                "WITH \"cache_name=" + financialInstrumentCacheName + "," +
                "value_type=financial_instrument\"";
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
                "                financial_instrument_in_count decimal,\n" +
                "                financial_instrument_out_count decimal,\n" +
                "                reduced_price_in     decimal   ,\n" +
                "                reduced_price_out    decimal   ,\n" +
                "                direction            varchar,\n" +
                "        primary key (market_depth_id, financial_instrument_in)\n" +
                ")" +
                "WITH \"affinitykey=financial_instrument_in,cache_name=" + marketDepthCacheName + "," +
                "key_type=MarketDepthKey,value_type=MarketDepth\"";
        node.cache(stockExchangeCacheName).query(new SqlFieldsQuery(sql)).getAll();
    }

    private static void insertMarketDepths() {
        //marketDepthId=1, stockExchangeId=1, financialInstrumentIn=1, financialInstrumentOut=2, orderDate=Fri Jun 22 20:40:40 MSK 2018, orderTime=01:01:01, financialInstrumentInCount=1.0, financialInstrumentOutCount=1.25, reducedPriceIn=1.0, reducedPriceOut=1.25, direction='sell'
        node.cache(marketDepthCacheName).query(new SqlFieldsQuery(
                "INSERT INTO \"PUBLIC\"." + marketDepthCacheName + "" +
                        "(market_depth_id, stock_exchange_id, financial_instrument_in, " +
                        "financial_instrument_out, order_date, order_time," +
                        "financial_instrument_in_count, financial_instrument_out_count, direction," +
                        "reduced_price_in, reduced_price_out) " +
                        "VALUES(1, 1, 1, " +
                        "1, '2018-01-01', '20:40:50'," +
                        "1, 2, 'sell'," +
                        "1,1)"));
        node.cache(marketDepthCacheName).query(new SqlFieldsQuery(
                "INSERT INTO \"PUBLIC\"." + marketDepthCacheName + "" +
                        "(market_depth_id, stock_exchange_id, financial_instrument_in, " +
                        "financial_instrument_out, order_date, order_time," +
                        "financial_instrument_in_count, financial_instrument_out_count, direction," +
                        "reduced_price_in, reduced_price_out) " +
                        "VALUES(2, 1, 2, " +
                        "1, '2018-01-01', '20:40:50'," +
                        "1, 23, 'sell'," +
                        "1,1)"));
        node.cache(marketDepthCacheName).query(new SqlFieldsQuery(
                "INSERT INTO \"PUBLIC\"." + marketDepthCacheName + "" +
                        "(market_depth_id, stock_exchange_id, financial_instrument_in, " +
                        "financial_instrument_out, order_date, order_time," +
                        "financial_instrument_in_count, financial_instrument_out_count, direction," +
                        "reduced_price_in, reduced_price_out) " +
                        "VALUES(3, 1, 2, " +
                        "1, '2018-01-01', '20:40:50'," +
                        "1, 28, 'sell'," +
                        "1,1)"));
    }

    private static void viewMarketDepths() {
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