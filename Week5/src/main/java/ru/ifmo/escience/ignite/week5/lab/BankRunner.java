package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheKeyConfiguration;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static java.util.Arrays.*;
import static java.util.Collections.singleton;

public class BankRunner {
    public static void main(String[] args) {
        CacheConfiguration<Integer, Bank> bankConfig = new CacheConfiguration<>("Bank");
        bankConfig.setIndexedTypes(Integer.class, Bank.class); //doing things the easy way

        CacheConfiguration<Integer, BankAccount> bankAccountConfig = new CacheConfiguration<>("BankAccount");
        //doing thing the hard way
        bankAccountConfig.setQueryEntities(singleton(
                new QueryEntity()
                        .setKeyType(BankAccountKey.class.getName())
                        .setValueType(BankAccount.class.getName())
                        .addQueryField("bankAccountKey", BankAccountKey.class.getName(), "bankAccountKey")
                        .addQueryField("bankAccountKey.id" , Integer.class.getName(), "id")
                        .addQueryField("bankAccountKey.bankId", Integer.class.getName(), "bankId")
                        .addQueryField("ccy", String.class.getName(), "ccy")
                        .addQueryField("amount", Double.class.getName(), "amount")
                        .setIndexes(asList(new QueryIndex("bankAccountKey"), new QueryIndex("ccy")))))
                .setCacheMode(CacheMode.PARTITIONED)
                .setKeyConfiguration(new CacheKeyConfiguration(BankAccountKey.class.getName(), "bankId"));
        CacheConfiguration<ClientKey, Client> clientConfig = new CacheConfiguration<>("Client");
        clientConfig.setIndexedTypes(ClientKey.class, Client.class);

        Ignite node = Ignition.start("Week5/config/default-client.xml");
        try {
            node.createCaches(asList(bankConfig, bankAccountConfig, clientConfig));
            node.cache("Bank").query(new SqlFieldsQuery("CREATE TABLE if not exists \"PUBLIC\".EuroRate(ccy varchar, rate double, " +
                    "primary key(ccy)) WITH \"template=REPLICATED,cache_name=EuroRate,value_type=EuroRate\""));

            node.cache("Bank").putAll(createBanks());
            node.cache("BankAccount").putAll(createAccounts());
            node.cache("Client").putAll(createClients());
            node.cache("Bank").query(new SqlFieldsQuery(createRates()));

            System.out.println(node.cache("BankAccount").get(3));
            System.out.println(node.cache("Bank").query(new SqlFieldsQuery("SELECT * FROM \"PUBLIC\".EuroRate")).getAll());
            System.out.println(node.cache("Bank").query(new SqlFieldsQuery("SELECT DISTINCT BankAccount.ID from " +
                    "\"BankAccount\".BANKACCOUNT BankAccount " +
                    "INNER JOIN Bank ON Bank.ID = BankAccount.BANKID " +
                    "WHERE Bank.NAME = 'TinkoffBank'")).getAll());
            System.out.println(node.cache("Bank").query(new SqlFieldsQuery("SELECT DISTINCT FULLNAME from " +
                    "\"Client\".CLIENT Client " +
                    "INNER JOIN \"BankAccount\".BANKACCOUNT BankAccount ON BankAccount.ID = Client.BANKACCOUNTID " +
                    "INNER JOIN Bank ON Bank.ID = BankAccount.BANKID " +
                    "WHERE Bank.NAME = 'TinkoffBank'")).getAll());
        } finally {
            node.cache("Bank").query(new SqlFieldsQuery("DROP TABLE if exists \"PUBLIC\".EuroRate"));
            node.destroyCaches(asList("Bank", "BankAccount", "Client"));
            node.close();
        }
    }

    @NotNull
    private static String createRates() {
        return "insert into \"PUBLIC\".EuroRate(ccy, rate) values" +
                "('EUR', 1)," +
                "('USD', 1.23)," +
                "('RUB', 70.9)," +
                "('GBP', 0.87)";
    }

    private static Map<Integer, Bank> createBanks() {
        HashMap<Integer, Bank> banks = new HashMap<>();
        banks.put(1, new Bank(1, "TinkoffBank", 8));
        banks.put(2, new Bank(2, "SberBank", 9));
        banks.put(3, new Bank(3, "RocketBank", 6));
        return banks;
    }

    private static Map<BankAccountKey, BankAccount> createAccounts() {
        HashMap<BankAccountKey, BankAccount> accounts = new HashMap<>();
        accounts.put(new BankAccountKey(1, 1), new BankAccount(new BankAccountKey(1, 1), "RUB", 72_000));
        accounts.put(new BankAccountKey(2, 1), new BankAccount(new BankAccountKey(2, 1), "EUR", 5_000));
        accounts.put(new BankAccountKey(3, 2), new BankAccount(new BankAccountKey(3, 2), "RUB", 104_000));
        accounts.put(new BankAccountKey(4, 3), new BankAccount(new BankAccountKey(4, 3), "RUB", 53_000));
        accounts.put(new BankAccountKey(5, 1), new BankAccount(new BankAccountKey(5, 1), "USD", 8_500));
        return accounts;
    }

    private static Map<ClientKey, Client> createClients() {
        HashMap<ClientKey, Client> clients = new HashMap<>();
        clients.put(new ClientKey(1, 5), new Client(new ClientKey(1, 5), "John Smith"));
        clients.put(new ClientKey(2, 3), new Client(new ClientKey(2, 3), "Jill Doe"));
        clients.put(new ClientKey(3, 1), new Client(new ClientKey(3, 1), "Jill Doe"));
        clients.put(new ClientKey(4, 4), new Client(new ClientKey(4, 4), "Medved Balalaikin"));
        clients.put(new ClientKey(5, 2), new Client(new ClientKey(5, 2), "Sample Name"));
        return clients;
    }
}
