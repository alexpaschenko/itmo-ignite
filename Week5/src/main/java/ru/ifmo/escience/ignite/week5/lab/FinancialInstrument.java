package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.util.Date;

/*
* if exists(select 1 from sys.sysforeignkey where role='FK_FINANCIA_REFERENCE_STOCK_EX') then
    alter table financial_instrument
       delete foreign key FK_FINANCIA_REFERENCE_STOCK_EX
end if;

drop table if exists financial_instrument;

create table financial_instrument
        (
        id                   int                            not null,
        stock_exchange_id    int                            null,
        name                 varchar                        null,
        short_name           varchar                        null,
        date_foundation      date                           null,
        constraint PK_FINANCIAL_INSTRUMENT primary key clustered (id)
        );

        alter table financial_instrument
        add constraint FK_FINANCIA_REFERENCE_STOCK_EX foreign key (stock_exchange_id)
        references stock_exchange (id)
        on update restrict
        on delete restrict;

        *
* */
public class FinancialInstrument {

    @QuerySqlField(index = true)
    private final long financialInstrumentId;
    @QuerySqlField(index = true)
    private final long stockExchangeId;
    @QuerySqlField(index = true)
    private final String name;
    @QuerySqlField(index = true)
    private final String shortName;
    @QuerySqlField(index = true)
    private final Date dateFoundation;

    public FinancialInstrument(long financialInstrumentId, long stockExchangeId, String name, String shortName, Date dateFoundation) {
        this.financialInstrumentId = financialInstrumentId;
        this.stockExchangeId = stockExchangeId;
        this.name = name;
        this.shortName = shortName;
        this.dateFoundation = dateFoundation;
    }

    public long getFinancialInstrumentId() {
        return financialInstrumentId;
    }

    public long getStockExchangeId() {
        return stockExchangeId;
    }

    public Date getDateFoundation() {
        return dateFoundation;
    }

    public String getShortName() {

        return shortName;
    }

    public String getName() {
        return name;
    }
}
/*if exists(select 1 from sys.sysforeignkey where role='FK_FINANCIA_REFERENCE_FINANCIA') then
    alter table financial_equivalents
       delete foreign key FK_FINANCIA_REFERENCE_FINANCIA
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_FINANCIA_REFERENCE_FINANCIA') then
    alter table financial_equivalents
       delete foreign key FK_FINANCIA_REFERENCE_FINANCIA
end if;

drop table if exists financial_equivalents;




create table financial_equivalents
        (
                financial_instrument_one int                            not null,
                financial_instrument_two int                            not null,
                constraint PK_FINANCIAL_EQUIVALENTS primary key clustered (financial_instrument_one, financial_instrument_two)
);

        alter table financial_equivalents
        add constraint FK_FINANCIA_REFERENCE_FINANCIA foreign key (financial_instrument_one)
        references financial_instrument (id)
        on update restrict
        on delete restrict;

        alter table financial_equivalents
        add constraint FK_FINANCIA_REFERENCE_FINANCIA foreign key (financial_instrument_two)
        references financial_instrument (id)
        on update restrict
        on delete restrict;
        */