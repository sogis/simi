package ch.so.agi.simi.core.entity;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.entity.data.ModelSchema;
import ch.so.agi.simi.entity.data.PostgresDB;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
// See https://doc.cuba-platform.com/manual-7.2/integration_tests_mw.html

class DeleteRules_Data {

    @RegisterExtension
    static TestContainer container = SimiTestContainer.Common.INSTANCE;
    static DataManager dataManager;

    @BeforeAll
    static void beforeAll() {
        dataManager = AppBeans.get(DataManager.class);
    }

    @Test
    void DeleteRule_DefaultIs_Deny() {  // Am Beispiel Database - ModelSchema

        Persistence pers = container.persistence();
        UUID dbId = null;
        UUID schemaId = null;

        try {
            try (Transaction trans = pers.createTransaction()) {
                EntityManager orm = pers.getEntityManager();

                PostgresDB db = container.metadata().create(PostgresDB.class);
                db.setDbName("inttest-db");
                db.setDefaultValue(false);

                ModelSchema ms = container.metadata().create(ModelSchema.class);
                ms.setSchemaName("inttest-schema");
                ms.setPostgresDB(db);

                orm.persist(db);
                orm.persist(ms);

                dbId = db.getId();
                schemaId = ms.getId();

                trans.commit();
            }

            try (Transaction trans = pers.createTransaction()) {
                EntityManager orm = pers.getEntityManager();

                PostgresDB persistedDb = dataManager.load(PostgresDB.class).id(dbId).one();
                orm.remove(persistedDb);

                //Assertions.assertThrows(Exception.class, () -> { trans.commit(); });
                trans.commit();
            }
        }
        finally {
            //clean up

            /*
            try(Transaction t = pers.createTransaction()) {
                EntityManager orm = pers.getEntityManager();
                if (schemaId != null) {
                    Optional<ModelSchema> ms = dataManager.load(ModelSchema.class).id(schemaId).optional();
                    if (ms.isPresent())
                        orm.remove(ms.get());
                }

                if (dbId != null) {
                    Optional<PostgresDB> db = dataManager.load(PostgresDB.class).id(dbId).optional();
                    if (db.isPresent())
                        orm.remove(db.get());
                }
                t.commit();
            }*/


        }
    }

    /*
            Customer customer = cont.metadata().create(Customer.class);
        customer.setName("c1");

        Customer committedCustomer = dataManager.commit(customer);
        assertEquals(customer, committedCustomer);

        Customer loadedCustomer = dataManager.load(Id.of(customer)).one();
        assertEquals(customer, loadedCustomer);

        dataManager.remove(loadedCustomer);
     */
}