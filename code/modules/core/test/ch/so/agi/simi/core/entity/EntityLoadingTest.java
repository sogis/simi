package ch.so.agi.simi.core.entity;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.entity.data.*;
import ch.so.agi.simi.entity.dependency.*;
import ch.so.agi.simi.entity.iam.Group;
import ch.so.agi.simi.entity.iam.Role;
import ch.so.agi.simi.entity.iam.User;
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
                .setView("rasterDs-edit");

        dataManager.loadList(context);
    }

    @Test
    void group_Browse_OK() {
        LoadContext context = LoadContext.create(Group.class)
                .setQuery(queryFor(Group.NAME))
                .setView("group_browse");

        dataManager.loadList(context);
    }

    @Test
    void group_Edit_OK() {
        LoadContext context = LoadContext.create(Group.class)
                .setQuery(queryFor(Group.NAME))
                .setView("group-edit");

        dataManager.loadList(context);
    }

    @Test
    void user_Browse_OK() {
        LoadContext context = LoadContext.create(User.class)
                .setQuery(queryFor(User.NAME))
                .setView("user-browse");

        dataManager.loadList(context);
    }

    @Test
    void user_Edit_OK() {
        LoadContext context = LoadContext.create(User.class)
                .setQuery(queryFor(User.NAME))
                .setView("user-edit");

        dataManager.loadList(context);
    }

    @Test
    void role_Browse_OK() {
        LoadContext context = LoadContext.create(Role.class)
                .setQuery(queryFor(Role.NAME))
                .setView("role-browse");

        dataManager.loadList(context);
    }

    @Test
    void role_Edit_OK() {
        LoadContext context = LoadContext.create(Role.class)
                .setQuery(queryFor(Role.NAME))
                .setView("role-edit");

        dataManager.loadList(context);
    }

    @Test
    void dependency_Browse_OK() {
        LoadContext context = LoadContext.create(Dependency.class)
                .setQuery(queryFor(Dependency.NAME))
                .setView("dependency-browse");

        dataManager.loadList(context);
    }

    @Test
    void ccc_Edit_OK() {
        LoadContext context = LoadContext.create(CCCIntegration.class)
                .setQuery(queryFor(CCCIntegration.NAME))
                .setView("ccc-edit");

        dataManager.loadList(context);
    }

    @Test
    void component_Edit_OK() {
        LoadContext context = LoadContext.create(Component.class)
                .setQuery(queryFor(Component.NAME))
                .setView("component-edit");

        dataManager.loadList(context);
    }

    @Test
    void featureInfo_Edit_OK() {
        LoadContext context = LoadContext.create(FeatureInfo.class)
                .setQuery(queryFor(FeatureInfo.NAME))
                .setView("featureInfo-edit");

        dataManager.loadList(context);
    }

    @Test
    void relation_Edit_OK() {
        LoadContext context = LoadContext.create(Relation.class)
                .setQuery(queryFor(Relation.NAME))
                .setView("relation-edit");

        dataManager.loadList(context);
    }

    @Test
    void report_Edit_OK() {
        LoadContext context = LoadContext.create(Report.class)
                .setQuery(queryFor(Report.NAME))
                .setView("report-edit");

        dataManager.loadList(context);
    }



    private static LoadContext.Query queryFor(String entityName){
        String queryStr = MessageFormat.format("select e from {0} e", entityName);
        return LoadContext.createQuery(queryStr);
    }
}