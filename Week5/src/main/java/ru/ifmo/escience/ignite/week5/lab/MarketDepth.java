package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.sql.Time;
import java.util.Date;

public class MarketDepth {
    @QuerySqlField
    private final long marketDepthId;
    @QuerySqlField
    private final long stockExchangeId;
    @QuerySqlField
    private final long financialInstrumentIn;
    @QuerySqlField
    private final long financialInstrumentOut;
    @QuerySqlField
    private final Date orderDate;
    @QuerySqlField
    private final Time orderTime;
    @QuerySqlField
    private final float financialInstrumentInCount;
    @QuerySqlField
    private final float financialInstrumentOutCount;
    @QuerySqlField
    private final float reducedPriceIn;
    @QuerySqlField
    private final float reducedPriceOut;


    public MarketDepth(long marketDepthId, long stockExchangeId, long financialInstrumentIn, long financialInstrumentOut, Date orderDate, Time orderTime, float financialInstrumentInCount, float financialInstrumentOutCount, float reducedPriceIn, float reducedPriceOut) {
        this.marketDepthId = marketDepthId;
        this.stockExchangeId = stockExchangeId;
        this.financialInstrumentIn = financialInstrumentIn;
        this.financialInstrumentOut = financialInstrumentOut;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.financialInstrumentInCount = financialInstrumentInCount;
        this.financialInstrumentOutCount = financialInstrumentOutCount;
        this.reducedPriceIn = reducedPriceIn;
        this.reducedPriceOut = reducedPriceOut;
    }

    public long getMarketDepthId() {
        return marketDepthId;
    }

    public long getStockExchangeId() {
        return stockExchangeId;
    }

    public long getFinancialInstrumentIn() {
        return financialInstrumentIn;
    }

    public long getFinancialInstrumentOut() {
        return financialInstrumentOut;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Time getOrderTime() {
        return orderTime;
    }

    public float getFinancialInstrumentInCount() {
        return financialInstrumentInCount;
    }

    public float getFinancialInstrumentOutCount() {
        return financialInstrumentOutCount;
    }

    public float getReducedPriceIn() {
        return reducedPriceIn;
    }

    public float getReducedPriceOut() {
        return reducedPriceOut;
    }
}
/*
if exists(select 1 from sys.sysforeignkey where role='FK_MARKET_D_REFERENCE_FINANCIA') then
    alter table market_depth
       delete foreign key FK_MARKET_D_REFERENCE_FINANCIA
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_MARKET_D_REFERENCE_FINANCIA') then
    alter table market_depth
       delete foreign key FK_MARKET_D_REFERENCE_FINANCIA
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_MARKET_D_REFERENCE_STOCK_EX') then
    alter table market_depth
       delete foreign key FK_MARKET_D_REFERENCE_STOCK_EX
end if;

drop table if exists market_depth;


create table market_depth
        (
                id                   bigint                         not null,
                stock_exchange_id    int                            null,
                financial_instrument_in int                            null,
                financial_instrument_out int                            null,
                order_date           date                           null,
                order_time           time                           null,
                financial_instrument_in_count float                          null,
                financial_instrument_out_count float                          null,
                reduced_price_in     decimal                        null,
                reduced_price_out    char(10)                       null,
        direction            varchar                        null,
        constraint PK_MARKET_DEPTH primary key clustered (id)
        );

        alter table market_depth
        add constraint FK_MARKET_D_REFERENCE_FINANCIA foreign key (financial_instrument_in)
        references financial_instrument (id)
        on update restrict
        on delete restrict;

        alter table market_depth
        add constraint FK_MARKET_D_REFERENCE_FINANCIA foreign key (financial_instrument_out)
        references financial_instrument (id)
        on update restrict
        on delete restrict;

        alter table market_depth
        add constraint FK_MARKET_D_REFERENCE_STOCK_EX foreign key (stock_exchange_id)
        references stock_exchange (id)
        on update restrict
        on delete restrict;
        */