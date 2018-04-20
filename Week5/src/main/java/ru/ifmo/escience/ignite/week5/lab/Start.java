package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CacheKeyConfiguration;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import ru.ifmo.escience.ignite.Utils;
import org.apache.ignite.cache.affinity.AffinityKey;

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

import static ru.ifmo.escience.ignite.Utils.print;


/*
* Main points


Cfg classes:

CacheConfiguration

QueryEntity

CacheKeyConfiguration (new)


@AffinityKeyMapped annotation (for types config)


Ignite SQL docs: CREATE TABLE params


4-5 entities created via classes/query entity/SQL


PARTITIONED/REPLICATED caches


Data colocation


Data load via cache API/SQL


SQL queries with joins
*
* */
public class Start {
    private static String ignitionConfig = "Week5/config/default-client.xml";
    private static String stockExchangeCacheName = "StockExchange";
    private static String financialInstrumentCacheName = "FinancialInstrument";
    private static String marketDepthCacheName = "MarketDepth";
    private static Ignite node;

    public static void main(String[] args) {
        node = Ignition.start(ignitionConfig);
        /*System.out.println(node.cacheNames());
        node.cache(financialInstrumentCacheName).query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".financial_instrument"));
        node.cache("financial_instrument").clear();
        node.destroyCaches(node.cacheNames());*/
        try {
            run();
        } finally {
            System.out.println(node.cacheNames());
            createOrGetAllCaches();
            dropAllTables();
            node.destroyCaches(node.cacheNames());
            System.out.println("destroyCaches" + node.cacheNames());
            node.close();
        }
    }

    private static void clearCaches() {
        node.cache(financialInstrumentCacheName).clear();
        node.cache(stockExchangeCacheName).clear();
        node.cache(marketDepthCacheName).clear();
    }

    private static void createOrGetAllCaches() {
        node.getOrCreateCache(financialInstrumentCacheName);
        node.getOrCreateCache(stockExchangeCacheName);
        node.getOrCreateCache(marketDepthCacheName);
    }

    private static void dropAllTables() {
        node.cache(financialInstrumentCacheName).query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".financial_instrument"));
        node.cache(stockExchangeCacheName).query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".stock_exchange"));
        node.cache(marketDepthCacheName).query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".market_depth"));
    }

    private static void run() {
        System.out.println(node.cacheNames());
        System.out.println("createStockExchange");
        createStockExchange();
        System.out.println("createFinancialInstrument");
        createFinancialInstrument();
        System.out.println("createMarketDepth");
        createMarketDepth();
        System.out.println(node.cacheNames());
    }

    private static void createStockExchange() {
        node.getOrCreateCache(stockExchangeCacheName);

        CacheConfiguration<AffinityKey<Long>, StockExchange> colStockExchangeCfg =
                new CacheConfiguration<>(stockExchangeCacheName);
        colStockExchangeCfg.setCacheMode(CacheMode.PARTITIONED);
        colStockExchangeCfg.setIndexedTypes(AffinityKey.class, StockExchange.class);

        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        cfg.setCacheConfiguration(colStockExchangeCfg);
        node.getOrCreateCache(colStockExchangeCfg);

        StockExchange exmo = new StockExchange(1, "exmo", "exmo.me");
        node.cache(stockExchangeCacheName).put(exmo.getKey(), exmo);

        Object b = node.cache(stockExchangeCacheName).get(exmo.getKey());
        System.out.println(b.toString());
    }


    private static void createFinancialInstrument() {
        node.getOrCreateCache(financialInstrumentCacheName);
        createFinancialInstrumentTable();
        insertFinancialInstruments();
        viewFinancialInstruments();
        System.out.println(node.cacheNames());
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
                "key_type=FinancialInstrumentKey,value_type=FinancialInstrument\"";
        node.cache(financialInstrumentCacheName)
                .query(new SqlFieldsQuery(sql))
                .getAll();
    }

    private static void insertFinancialInstruments() {
        System.out.println(node.cacheNames());
        node.cache(financialInstrumentCacheName).query(new SqlFieldsQuery(
                "INSERT INTO \"PUBLIC\".financial_instrument(financial_instrument_id, name, short_name) " +
                        "VALUES(2, 'Bitcoin', 'BTC')"));
        //.setArgs(2, "Bitcoin", "BTC", new Date()));


        String sqlInsert = "INSERT INTO \"PUBLIC\".financial_instrument(financial_instrument_id, name, short_name, date_foundation) VALUES(?, ?, ?, ?)";
        SqlFieldsQuery a = new SqlFieldsQuery(sqlInsert).setArgs(new AffinityKey<>((long) 1), "USD", "USD", new Date());
        node.cache(financialInstrumentCacheName)
                .query(a);

        //node.cache(financialInstrumentCacheName)
          //      .query(new SqlFieldsQuery(sqlInsert).setArgs(2, "Bitcoin", "BTC", new Date()));
    }

    private static void viewFinancialInstruments() {
        System.out.println(node.cache(financialInstrumentCacheName)
                .query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\".financial_instrument"))
                .getAll());
    }

    private static void createMarketDepth() {
        node.getOrCreateCache(marketDepthCacheName);
        createMarketDepthTable();
        insertMarketDepths();
        viewMarketDepths();
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
                "key_type=MarketDepthKey,value_type=MarketDepth\"";
        node.cache(marketDepthCacheName).query(new SqlFieldsQuery(sql)).getAll();
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
                .query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\".market_depth"))
                .getAll());
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

        BinaryObject ck = node.binary().builder("FinancialInstrumentKey")
                .setField("FINANCIAL_INSTRUMENT_ID", (long) 1)
                .build();
        FinancialInstrument a = new FinancialInstrument(1, 1, "dsf", "dsad", new Date());

        node.cache(financialInstrumentCacheName).put(ck, a);
        Object b = node.cache(financialInstrumentCacheName).get(ck);
        System.out.println(node.cache(financialInstrumentCacheName).get(ck));
        print(Utils.sameAffinity(node, financialInstrumentCacheName, 1, "financial_instrument", ck));

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

    /*private static void createStockExchangeTable() {
        String sql = "CREATE TABLE if not exists \"PUBLIC\".stock_exchange" +
                "(" +
                "stock_exchange_id long, " +
                "name varchar, " +
                "primary_domain varchar, " +
                "primary key(stock_exchange_id)) " +
                "WITH \"affinitykey=STOCK_EXCHANGE_ID,cache_name=stock_exchange," +
                "key_type=StockExchangeKey,value_type=StockExchange\"";
        node.cache(stockExchangeCacheName).query(new SqlFieldsQuery(sql)).getAll();

        BinaryObject exmoKey = node.binary().builder("StockExchangeKey")
                .setField("STOCK_EXCHANGE_ID", 1)
                .build();
        StockExchange exmo = new StockExchange(1, "exmo", "exmo.me");
        node.cache(stockExchangeCacheName).put(exmoKey, exmo);
    }
    private static void showAllRecords() {
        System.out.println(node.cache(stockExchangeCacheName).query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\".stock_exchange")).getAll());
        System.out.println(node.cache(marketDepthCacheName).query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\".market_depth")).getAll());
        System.out.println(node.cache(financialInstrumentCacheName).query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\".financial_instrument")).getAll());
    }

  private static void insertStockExchanges() {
        BinaryObject exmoKey = node.binary().builder("StockExchangeKey")
                .setField("STOCK_EXCHANGE_ID", (long) 1)
                .build();
        StockExchange exmo = new StockExchange(1, "exmo", "exmo.me");
        node.cache(stockExchangeCacheName).put(exmoKey, exmo);

        BinaryObject bitfenixKey = node.binary().builder("StockExchangeKey")
                .setField("STOCK_EXCHANGE_ID", 2)
                .build();
        StockExchange bitfenix = new StockExchange(2, "Bitfenix", "bitfenix.com");
        node.cache(stockExchangeCacheName).put(bitfenixKey, bitfenix);
    }
    */