package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class StockExchange {
    @QuerySqlField(index = true)
    private final long stockExchangeId;
    @QuerySqlField
    private final String name;
    @QuerySqlField
    private final String primaryDomain;

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
}
