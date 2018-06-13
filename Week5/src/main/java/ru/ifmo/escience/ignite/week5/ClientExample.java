package ru.ifmo.escience.ignite.week5;
import java.util.*;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import ru.ifmo.escience.ignite.Utils;
import ru.ifmo.escience.ignite.week4.Person;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import java.io.IOException;

import static ru.ifmo.escience.ignite.Utils.print;
import static ru.ifmo.escience.ignite.Utils.readln;

import java.util.List;

public  class ClientExample {

    @SuppressWarnings("ThrowFromFinallyBlock")
    public static void main(String[] args) throws IOException {
        CacheConfiguration<Integer, Person> ccfg = new CacheConfiguration<>("Person");
        ccfg.setIndexedTypes(Integer.class, Person.class);
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        cfg.setCacheConfiguration(ccfg);
        Ignite node = Ignition.start("Week5/config/default-client.xml");
        node.getOrCreateCache(ccfg);
        int key = 1;
        try {
//            create some table
            node.cache("Person").query(new SqlFieldsQuery("CREATE TABLE if not exists \"PUBLIC\".Post(id int, person_id int, " +
                    "text varchar,time datetime,like_count int, primary key(id, person_id)) WITH \"affinitykey=PERSON_ID,cache_name=Post," +
                    "key_type=PostKey,value_type=Post\""));

//            Put persons by cashe.put

            Person person = new Person("Artem", "Pavlov", "Retew","Retew");
            node.cache("Person").put(key, person);
            key = key + 1;
            Person person1 = new Person("Semen", "Pavlov", "semenPavlov","semenPavlov");
            node.cache("Person").put(key, person1);
            key = key + 1;
            Person person2 = new Person("Mary", "Retova", "mary","stas");
            node.cache("Person").put(key, person2);
            key = key + 1;
            Person person3 = new Person("Artem", "Pavlov", "Retew","Retew");
            node.cache("Person").put(key, person3);
            key = key + 1;


//                insert by SqlFieldsQuery
            node.cache("Person").query(new SqlFieldsQuery("insert into \"PUBLIC\".Post(id, person_id, text,time,like_count) " +
                    "values(?,?,?,?,?)").setArgs(1, 2, "blabla","1999-10-10",34));
            node.cache("Person").query(new SqlFieldsQuery("insert into \"PUBLIC\".Post(id, person_id, text,time,like_count) " +
                    "values(?,?,?,?,?)").setArgs(2, 1, "blablabla","1998-05-10",56));
            node.cache("Person").query(new SqlFieldsQuery("insert into \"PUBLIC\".Post(id, person_id, text,time,like_count) " +
                    "values(?,?,?,?,?)").setArgs(3, 3, "blablablabla","1997-02-10",11));
            node.cache("Person").query(new SqlFieldsQuery("insert into \"PUBLIC\".Post(id, person_id, text,time,like_count) " +
                    "values(?,?,?,?,?)").setArgs(4, 4, "blablablablabla","1998-02-10",11));



            String sql = "select * from \"PUBLIC\".Post ";
//            printSQL
            try (FieldsQueryCursor<List<?>> cursor =  node.cache("Person").query(new SqlFieldsQuery(sql))) {
                for (List<?> row : cursor) {
                    System.out.println(row);
                }
            }
            System.out.println();

            sql = "select * from \"PUBLIC\".Post group by person_id";
            System.out.println(node.cache("Person").query(new SqlFieldsQuery(sql)).getAll());


        }
        finally {
            node.cache("Person").query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".Post"));

            node.close();
        }
        readln();

    }



}