package ch.so.agi.simi.core.entity;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.entity.data.PostgresDB;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;
// See https://doc.cuba-platform.com/manual-7.2/integration_tests_mw.html

class EntityLoadingTest {

    @RegisterExtension
    static TestContainer container = SimiTestContainer.Common.INSTANCE;
    static DataManager dataManager;

    @BeforeAll
    static void beforeAll() {
        dataManager = AppBeans.get(DataManager.class);
    }

    @Test
    void postgresDb_Browse_OK() {
        LoadContext context = LoadContext.create(PostgresDB.class)
                .setQuery(queryFor(PostgresDB.NAME))
                .setView("postgresDB-browse");

        dataManager.loadList(context);
    }

    @Test
    void postgresDb_Edit_OK() {
        LoadContext context = LoadContext.create(PostgresDB.class)
                .setQuery(queryFor(PostgresDB.NAME))
                .setView("postgresDB-edit");

        dataManager.loadList(context);
    }

    private static LoadContext.Query queryFor(String entityName){
        String queryStr = MessageFormat.format("select e from {0} e", entityName);
        return LoadContext.createQuery(queryStr);
    }
}