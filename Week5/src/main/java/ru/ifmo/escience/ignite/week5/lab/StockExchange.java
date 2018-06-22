package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.affinity.AffinityKey;

public class StockExchange {
    @QuerySqlField(index = true)
    private final long stockExchangeId;
    @QuerySqlField(index = true)
    private final String name;
    @QuerySqlField(index = true)
    private final String primaryDomain;

    private transient AffinityKey<Long> key;

    public StockExchange(long stockExchangeId, String name, String primaryDomain) {
        this.stockExchangeId = stockExchangeId;
        this.name = name;
        this.primaryDomain = primaryDomain;
    }

    public long getStockExchangeId() {
        return stockExchangeId;
    }

    public String getName() {
        return name;
    }

    public String getPrimaryDomain() {
        return primaryDomain;
    }

    public Long getKey() {
        return stockExchangeId;
    }

    @Override
    public String toString() {
        return "StockExchange{" +
                "stockExchangeId=" + stockExchangeId +
                ", name='" + name + '\'' +
                ", primaryDomain='" + primaryDomain + '\'' +
                ", key=" + key +
                '}';
    }
}
