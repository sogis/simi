package ch.so.agi.simi.core.entity;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.entity.data.*;
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

    @Test
    void modelSchema_Browse_OK() {
        LoadContext context = LoadContext.create(ModelSchema.class)
                .setQuery(queryFor(ModelSchema.NAME))
                .setView("modelSchema-browse");

        dataManager.loadList(context);
    }

    @Test
    void modelSchema_Edit_OK() {
        LoadContext context = LoadContext.create(ModelSchema.class)
                .setQuery(queryFor(ModelSchema.NAME))
                .setView("modelSchema-edit");

        dataManager.loadList(context);
    }

    @Test
    void postgresTable_Browse_OK() {
        LoadContext context = LoadContext.create(PostgresTable.class)
                .setQuery(queryFor(PostgresTable.NAME))
                .setView("postgresTable-browse");

        dataManager.loadList(context);
    }

    @Test
    void postgresTable_Edit_OK() {
        LoadContext context = LoadContext.create(PostgresTable.class)
                .setQuery(queryFor(PostgresTable.NAME))
                .setView("postgresTable-edit");

        dataManager.loadList(context);
    }

    @Test
    void tableField_Edit_OK() {
        LoadContext context = LoadContext.create(TableField.class)
                .setQuery(queryFor(TableField.NAME))
                .setView("tableField-edit");

        dataManager.loadList(context);
    }

    @Test
    void externalTable_Browse_OK() {
        LoadContext context = LoadContext.create(ExternalTable.class)
                .setQuery(queryFor(ExternalTable.NAME))
                .setView("externalTable-browse");

        dataManager.loadList(context);
    }

    @Test
    void externalTable_Edit_OK() {
        LoadContext context = LoadContext.create(ExternalTable.class)
                .setQuery(queryFor(ExternalTable.NAME))
                .setView("externalTable-edit");

        dataManager.loadList(context);
    }

    @Test
    void rasterDs_Browse_OK() {
        LoadContext context = LoadContext.create(RasterDS.class)
                .setQuery(queryFor(RasterDS.NAME))
                .setView("rasterDs_browse");

        dataManager.loadList(context);
    }

    @Test
    void rasterDs_Edit_OK() {
        LoadContext context = LoadContext.create(RasterDS.class)
                .setQuery(queryFor(RasterDS.NAME))
                .setView("rasterDs_edit");

        dataManager.loadList(context);
    }












    private static LoadContext.Query queryFor(String entityName){
        String queryStr = MessageFormat.format("select e from {0} e", entityName);
        return LoadContext.createQuery(queryStr);
    }
}