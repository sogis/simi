package ch.so.agi.simi.core.copy;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.entity.data.ModelSchema;
import ch.so.agi.simi.entity.data.PostgresDB;
import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.data.TableField;
import ch.so.agi.simi.entity.dependency.Component;
import ch.so.agi.simi.entity.dependency.Relation;
import ch.so.agi.simi.entity.dependency.RelationType;
import ch.so.agi.simi.entity.iam.Permission;
import ch.so.agi.simi.entity.iam.PermissionLevelEnum;
import ch.so.agi.simi.entity.iam.Role;
import ch.so.agi.simi.entity.product.datasetview.DataSetView;
import ch.so.agi.simi.entity.product.datasetview.StyleAsset;
import ch.so.agi.simi.entity.product.datasetview.TableView;
import ch.so.agi.simi.entity.product.datasetview.ViewField;
import ch.so.agi.simi.entity.product.non_dsv.*;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
// See https://doc.cuba-platform.com/manual-7.2/integration_tests_mw.html

class CopyServiceBeanTest {

    private static final String TV_STRING = "ch.so.agi.inttest.tableview";
    private static final String TV_DB_STRING = "ch.so.agi.inttest.tableview.db";
    private static final String TV_SCHEMA_STRING = "ch.so.agi.inttest.tableview.schema";
    private static final String TV_TABLE_STRING = "ch.so.agi.inttest.tableview.table";
    private static final String TV_FL_STRING = "ch.so.agi.inttest.tableview.facadelayer";
    private static final String TV_LG_STRING = "ch.so.agi.inttest.tableview.layergroup";
    private static final String TV_ROLE_STRING = "ch.so.agi.inttest.tableview.role";
    private static final String TV_COMPONENT_STRING = "ch.so.agi.inttest.tableview.component";

    @RegisterExtension
    static TestContainer container = SimiTestContainer.Common.INSTANCE;
    static DataManager dataManager;

    @BeforeAll
    static void beforeAll() {
        dataManager = AppBeans.get(DataManager.class);
    }

    @Test
    void copy_TableView_AllPropsAndRefs_OK() {
        perpareTvTestdata();

        cleanupTvTestdata();
    }

    private void cleanupTvTestdata(){
        // tv löschen
        // db löschen
        // fl löschen
        // lg löschen
    }

    private UUID perpareTvTestdata(){

        Persistence pers = container.persistence();
        UUID dsvId = null;

        try (Transaction trans = pers.createTransaction()) {
            EntityManager orm = pers.getEntityManager();

            //PostgresTable
            PostgresDB db = container.metadata().create(PostgresDB.class);
            db.setDbName(TV_DB_STRING);

            ModelSchema ms = container.metadata().create(ModelSchema.class);
            ms.setSchemaName(TV_SCHEMA_STRING);
            ms.setPostgresDB(db);

            PostgresTable tbl = container.metadata().create(PostgresTable.class);
            tbl.setTableName(TV_TABLE_STRING);
            tbl.setIdFieldName("fuu");
            tbl.setCatSyncStamp(LocalDateTime.now());
            tbl.setModelSchema(ms);

            orm.persist(db);
            orm.persist(ms);
            orm.persist(tbl);

            // TableView
            TableView tv = container.metadata().create(TableView.class);
            tv.setGeomFieldName(TV_STRING);
            tv.setSearchFacet(TV_STRING);
            tv.setTransparency(88);
            tv.setIdentifier(TV_STRING);
            tv.setPostgresTable(tbl);

            DataProduct_PubScope ps = dataManager.load(DataProduct_PubScope.class).one();
            tv.setPubScope(ps);

            orm.persist(tv);

            //Fields
            TableField tf = container.metadata().create(TableField.class);
            tf.setName(TV_STRING);
            tf.setTypeName("bar");
            tf.setPostgresTable(tbl);

            ViewField vf = container.metadata().create(ViewField.class);
            vf.setSort(88);
            vf.setTableField(tf);
            vf.setTableView(tv);
            LinkedList<ViewField> fieldList = new LinkedList<>();
            fieldList.add(vf);
            tv.setViewFields(fieldList);

            orm.persist(tf);
            orm.persist(vf);

            //Assets
            StyleAsset sa = container.metadata().create(StyleAsset.class);
            sa.setFileContent(new byte[]{});
            sa.setFileName(TV_STRING);
            sa.setDatasetSetView(tv);
            LinkedList<StyleAsset> assets = new LinkedList<>();
            assets.add(sa);
            tv.setStyleAssets(assets);

            orm.persist(sa);

            //PropertiesInFacade
            FacadeLayer fl = container.metadata().create(FacadeLayer.class);
            fl.setIdentifier(TV_FL_STRING);
            fl.setPubScope(ps);

            PropertiesInFacade pif = container.metadata().create(PropertiesInFacade.class);
            pif.setFacadeLayer(fl);
            pif.setDataSetView(tv);
            pif.setSort(88);

            orm.persist(fl);
            orm.persist(pif);

            //PropertiesInList
            LayerGroup lg = container.metadata().create(LayerGroup.class);
            lg.setIdentifier(TV_LG_STRING);
            lg.setPubScope(ps);

            PropertiesInList pil = container.metadata().create(PropertiesInList.class);
            pil.setProductList(lg);
            pil.setSingleActor(tv);
            pil.setSort(88);

            orm.persist(lg);
            orm.persist(pil);

            //Permissions
            Role role = container.metadata().create(Role.class);
            role.setName(TV_ROLE_STRING);

            Permission perm = container.metadata().create(Permission.class);
            perm.setLevel(PermissionLevelEnum.READ);
            perm.setRole(role);
            perm.setDataSetView(tv);

            orm.persist(role);
            orm.persist(perm);

            //Dependencies
            Component comp = container.metadata().create(Component.class);
            comp.setName(TV_COMPONENT_STRING);

            Relation rel = container.metadata().create(Relation.class);
            rel.setRelationType(RelationType.DATA);
            rel.setDependency(comp);
            rel.setDataSetView(tv);

            orm.persist(comp);
            orm.persist(rel);

            trans.commit();
        }

        return dsvId;
    }
}