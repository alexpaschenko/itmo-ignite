package ru.ifmo.escience.ignite.week5;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import ru.ifmo.escience.ignite.Utils;
import ru.ifmo.escience.ignite.week4.Person;

import java.io.IOException;

import static ru.ifmo.escience.ignite.Utils.print;
import static ru.ifmo.escience.ignite.Utils.readln;


public class ClientExample {
    @SuppressWarnings("ThrowFromFinallyBlock")
    public static void main(String[] args) throws IOException {
        CacheConfiguration<Integer, Person> ccfg = new CacheConfiguration<>("Person");

        ccfg.setIndexedTypes(Integer.class, Person.class);

        IgniteConfiguration cfg = new IgniteConfiguration();

        cfg.setClientMode(true);

        cfg.setCacheConfiguration(ccfg);

        Ignite node1 = Ignition.start(cfg);

        try {
            node1.cache("Person").query(new SqlFieldsQuery("CREATE TABLE if not exists \"PUBLIC\".Car(id int, person_id int, " +
                "license varchar, primary key(id, person_id)) WITH \"affinitykey=PERSON_ID,cache_name=Car," +
                "key_type=CarKey,value_type=Car\""));

            Person p = new Person("A", "P", 200);

            node1.cache("Person").put(1, p);

            node1.cache("Person").query(new SqlFieldsQuery("insert into \"PUBLIC\".Car(id, person_id, license) " +
                    "values(1, 5, 'a111aa')"));

            BinaryObject ck = node1.binary().builder("CarKey").setField("ID", 1).setField("PERSON_ID", 100000).build();

            print(Utils.sameAffinity(node1, "Person", 1, "Car", ck));

            readln();
        }
        finally {
            node1.cache("Person").query(new SqlFieldsQuery("DROP TABLE IF EXISTS \"PUBLIC\".Car"));

            node1.close();
        }
    }
}
