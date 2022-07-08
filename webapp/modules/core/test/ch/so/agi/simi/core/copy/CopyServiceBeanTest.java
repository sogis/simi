package ch.so.agi.simi.core.copy;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.entity.data.*;
import ch.so.agi.simi.entity.extended.Component;
import ch.so.agi.simi.entity.extended.Relation;
import ch.so.agi.simi.entity.extended.RelationType;
import ch.so.agi.simi.entity.iam.Permission;
import ch.so.agi.simi.entity.iam.PermissionLevelEnum;
import ch.so.agi.simi.entity.iam.Role;
import ch.so.agi.simi.entity.data.datasetview.*;
import ch.so.agi.simi.entity.product.*;
import ch.so.agi.simi.entity.theme.Theme;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.entity.theme.ThemePublication_TypeEnum;
import ch.so.agi.simi.entity.theme.org.Agency;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

import static ch.so.agi.simi.core.copy.CopyServiceBean.*;
import ch.so.agi.simi.core.test.Util;
import static org.junit.jupiter.api.Assertions.*;
// See https://doc.cuba-platform.com/manual-7.2/integration_tests_mw.html

class CopyServiceBeanTest {

    private static final String TV_DP_STRING = "ch.so.agi.inttest.tableview";
    private static final String TV_DB_STRING = "ch.so.agi.inttest.tableview.db";
    private static final String TV_SCHEMA_STRING = "ch.so.agi.inttest.tableview.schema";
    private static final String TV_TABLE_STRING = "ch.so.agi.inttest.tableview.table";
    private static final String TV_ROLE_STRING = "ch.so.agi.inttest.tableview.role";
    private static final String TV_COMPONENT_STRING = "ch.so.agi.inttest.tableview.component";
    private static final String TV_FIELD_ALIAS = "ch.so.agi.inttest.tableview.field.alias";
    private static final int TV_SA_TRANSPARENCY = 88;

    private static final String RV_DP_STRING = "ch.so.agi.inttest.rasterview";
    private static final String RV_ROLE_STRING = "ch.so.agi.inttest.rasterview.role";
    private static final String RV_COMPONENT_STRING = "ch.so.agi.inttest.rasterview.component";
    private static final int RV_SA_TRANSPARENCY = 87;

    private static final String FL_DP_STRING = "ch.so.agi.inttest.facadelayer";
    private static final String FL_DSV_STRING = "ch.so.agi.inttest.facadelayer.datasetview";
    private static final int FL_SA_TRANSPARENCY = 86;

    private static final String LG_DP_STRING = "ch.so.agi.inttest.layergroup";
    private static final String LG_FL_STRING = "ch.so.agi.inttest.layergroup.facadelayer";

    private static final String MAP_DP_STRING = "ch.so.agi.inttest.map";
    private static final String MAP_FL_STRING = "ch.so.agi.inttest.map.facadelayer";

    @RegisterExtension
    static TestContainer container = SimiTestContainer.Common.INSTANCE;

    static DataManager dataManager;
    static CopyService serviceBean;

    static ThemePublication themePubForTest;

    @BeforeAll
    static void beforeAll() {

        dataManager = AppBeans.get(DataManager.class);
        serviceBean = AppBeans.get(CopyService.class);

        themePubForTest = Util.createThemePub(container, dataManager);
    }


    @AfterAll
    static void afterAll() {
        Util.removeThemePubs(dataManager);
    }



    @Test
    void copy_TableView_OK() throws Exception {

        try {
            cleanupTvTestdata();
            UUID tvId = prepareTvTestdata();

            UUID copyId = serviceBean.copyProduct(tvId);

            TableView tv = dataManager.load(TableView.class).view(TV_VIEW_NAME).id(copyId).one();

            assertTrue(tv.getDerivedIdentifier().contains(TV_DP_STRING), "Identifier of copy must start with identifier of the original");
            assertEquals(TV_DP_STRING, tv.getSearchFacet());
            assertEquals(TV_SA_TRANSPARENCY, tv.getTransparency());

            assertEquals(TV_COMPONENT_STRING, tv.getRelations().get(0).getDependency().getName());
            assertEquals(TV_ROLE_STRING, tv.getPermissions().get(0).getRole().getName());
        }
        finally {
            cleanupTvTestdata();
        }
    }

    @Test
    void copy_RasterView_OK() throws Exception {

        try {
            cleanupRvTestdata();

            UUID rvId = prepareRvTestdata();

            UUID copyId = serviceBean.copyProduct(rvId);

            RasterView rv = dataManager.load(RasterView.class).view(RV_VIEW_NAME).id(copyId).one();

            assertTrue(rv.getDerivedIdentifier().contains(RV_DP_STRING), "Identifier of copy must start with identifier of the original");
            assertEquals(RV_SA_TRANSPARENCY, rv.getTransparency());

            //assertEquals(RV_PL_STRING, rv.getProductLists().get(0).getProductList().getDerivedIdentifier());
            //assertEquals(RV_FL_STRING, rv.getFacadeLayers().get(0).getFacadeLayer().getDerivedIdentifier());

            assertEquals(RV_COMPONENT_STRING, rv.getRelations().get(0).getDependency().getName());
            assertEquals(RV_ROLE_STRING, rv.getPermissions().get(0).getRole().getName());
        }
        finally {
            cleanupRvTestdata();
        }
    }

    @Test
    void copy_FacadeLayer_OK() throws Exception {

        try {
            cleanupFlTestdata();
            UUID flId = prepareFlTestdata();

            UUID copyId = serviceBean.copyProduct(flId);

            FacadeLayer fl = dataManager.load(FacadeLayer.class).view(FL_VIEW_NAME).id(copyId).one();

            assertTrue(fl.getDerivedIdentifier().contains(FL_DP_STRING), "Identifier of copy must start with identifier of the original");
            assertEquals(FL_SA_TRANSPARENCY, fl.getTransparency());

            assertEquals(FL_DSV_STRING, fl.getDataSetViews().get(0).getDataSetView().getDerivedIdentifier());
        }
        finally {
            cleanupFlTestdata();
        }
    }

    @Test
    void copy_LayerGroup_OK() throws Exception {

        try {
            cleanupLgTestdata();
            UUID lgId = prepareLgTestdata();

            UUID copyId = serviceBean.copyProduct(lgId);

            LayerGroup lg = dataManager.load(LayerGroup.class).view(LG_VIEW_NAME).id(copyId).one();

            assertTrue(lg.getDerivedIdentifier().contains(LG_DP_STRING), "Identifier of copy must start with identifier of the original");
            
            assertEquals(LG_FL_STRING, lg.getSingleActors().get(0).getSingleActor().getDerivedIdentifier());
        }
        finally {
            cleanupLgTestdata();
        }
    }

    @Test
    void copy_Map_OK() throws Exception {

        try {
            cleanupMapTestdata();
            UUID mapId = prepareMapTestdata();

            UUID copyId = serviceBean.copyProduct(mapId);

            Map map = dataManager.load(Map.class).view(MAP_VIEW_NAME).id(copyId).one();

            assertTrue(map.getDerivedIdentifier().contains(MAP_DP_STRING), "Identifier of copy must start with identifier of the original");

            assertEquals(MAP_FL_STRING, map.getSingleActors().get(0).getSingleActor().getDerivedIdentifier());
        }
        finally {
            cleanupMapTestdata();
        }
    }

    private void cleanupRvTestdata(){

        Persistence pers = container.persistence();
        UUID dsvId = null;

        try (Transaction trans = pers.createTransaction()) {
            EntityManager orm = pers.getEntityManager();

            List<RasterView> rvs = dataManager.load(RasterView.class)
                    .query("select e from simiData_RasterView e where e.derivedIdentifier like :identifier")
                    .parameter("identifier", RV_DP_STRING + "%")
                    .view(RV_VIEW_NAME)
                    .list();

            RasterView curr = null;
            for (RasterView rv : rvs){
                curr = rv;
                orm.remove(rv);
            }

            if(curr != null){ //original and copy share the same relationships --> remove only once

                orm.remove( curr.getRasterDS() );

                orm.remove( curr.getPermissions().get(0).getRole() );
                orm.remove( curr.getRelations().get(0).getDependency() );
            }

            trans.commit();
        }
    }

    private void cleanupMapTestdata(){

        Persistence pers = container.persistence();
        UUID dsvId = null;

        try (Transaction trans = pers.createTransaction()) {
            EntityManager orm = pers.getEntityManager();

            List<Map> maps = dataManager.load(Map.class)
                    .query("select e from simiProduct_Map e where e.derivedIdentifier like :identifier")
                    .parameter("identifier", MAP_DP_STRING + "%")
                    .view(MAP_VIEW_NAME)
                    .list();

            Map curr = null;
            for (Map lg : maps){
                curr = lg;
                orm.remove(lg);
            }

            if(curr != null){ //original and copy share the same relationships --> remove only once
                orm.remove( curr.getSingleActors().get(0).getSingleActor() );
            }

            trans.commit();
        }
    }

    private void cleanupLgTestdata(){

        Persistence pers = container.persistence();
        UUID dsvId = null;

        try (Transaction trans = pers.createTransaction()) {
            EntityManager orm = pers.getEntityManager();

            List<LayerGroup> lgs = dataManager.load(LayerGroup.class)
                    .query("select e from simiProduct_LayerGroup e where e.derivedIdentifier like :identifier")
                    .parameter("identifier", LG_DP_STRING + "%")
                    .view(LG_VIEW_NAME)
                    .list();

            LayerGroup curr = null;
            for (LayerGroup lg : lgs){
                curr = lg;
                orm.remove(lg);
            }

            if(curr != null){ //original and copy share the same relationships --> remove only once
                orm.remove( curr.getSingleActors().get(0).getSingleActor() );
            }

            trans.commit();
        }
    }

    private void cleanupFlTestdata(){

        Persistence pers = container.persistence();
        UUID dsvId = null;

        try (Transaction trans = pers.createTransaction()) {
            EntityManager orm = pers.getEntityManager();

            List<FacadeLayer> rvs = dataManager.load(FacadeLayer.class)
                    .query("select e from simiProduct_FacadeLayer e where e.derivedIdentifier like :identifier")
                    .parameter("identifier", FL_DP_STRING + "%")
                    .view(FL_VIEW_NAME)
                    .list();

            FacadeLayer curr = null;
            for (FacadeLayer fl : rvs){
                curr = fl;
                orm.remove(fl);
            }

            if(curr != null){ //original and copy share the same relationships --> remove only once
                orm.remove( curr.getDataSetViews().get(0).getDataSetView() );
            }

            //Remove Raster-DS
            Optional<RasterDS> rds = dataManager.load(RasterDS.class)
                    .query("select e from simiData_RasterDS e where e.path = :path")
                    .parameter("path", FL_DP_STRING)
                    .optional();
            
            if(rds.isPresent())
                orm.remove( rds.get() );

            trans.commit();
        }
    }

    private void cleanupTvTestdata(){

        Persistence pers = container.persistence();
        UUID dsvId = null;

        try (Transaction trans = pers.createTransaction()) {
            EntityManager orm = pers.getEntityManager();

            List<TableView> tvs = dataManager.load(TableView.class)
                    .query("select e from simiData_TableView e where e.derivedIdentifier like :identifier")
                    .parameter("identifier", TV_DP_STRING + "%")
                    .view(TV_VIEW_NAME)
                    .list();

            TableView curr = null;
            for (TableView tv : tvs){
                curr = tv;
                orm.remove(tv);
            }

            if(curr != null){ //original and copy share the same relationships --> remove only once
                orm.remove( curr.getPostgresTable() );
                orm.remove( curr.getPostgresTable().getDbSchema() );
                orm.remove( curr.getPostgresTable().getDbSchema().getPostgresDB() );

                orm.remove( curr.getPermissions().get(0).getRole() );
                orm.remove( curr.getRelations().get(0).getDependency() );
            }

            trans.commit();
        }
    }

    private UUID prepareRvTestdata(){

        Persistence pers = container.persistence();
        UUID rvId = null;

        try (Transaction trans = pers.createTransaction()) {
            EntityManager orm = pers.getEntityManager();

            RasterDS rds = container.metadata().create(RasterDS.class);
            rds.setPath(UUID.randomUUID().toString());
            orm.persist(rds);

            // RasterView
            RasterView rv = container.metadata().create(RasterView.class);
            rvId = rv.getId();

            rv.setTransparency(RV_SA_TRANSPARENCY);
            setDprodFields(rv, RV_DP_STRING);

            DataProduct_PubScope ps = dataManager.load(DataProduct_PubScope.class).one();
            rv.setPubScope(ps);
            rv.setRasterDS(rds);

            rv.setThemePublication(themePubForTest);

            orm.persist(rv);

            //Assets
            StyleAsset sa = linkToStyleAsset(rv, RV_DP_STRING);
            orm.persist(sa);

            //Permissions
            BaseUuidEntity[] perm = linkToPermissions(rv, RV_ROLE_STRING);
            orm.persist(perm[0]); orm.persist(perm[1]);

            //Dependencies
            BaseUuidEntity[] dep = linkToDependencies(rv, RV_COMPONENT_STRING);
            orm.persist(dep[0]); orm.persist(dep[1]);

            trans.commit();
        }

        return rvId;
    }

    private UUID prepareLgTestdata(){

        Persistence pers = container.persistence();
        UUID lgId = null;

        try (Transaction trans = pers.createTransaction()) {
            EntityManager orm = pers.getEntityManager();

            // LayerGroup
            LayerGroup lg = container.metadata().create(LayerGroup.class);
            lgId = lg.getId();

            setDprodFields(lg, LG_DP_STRING);

            DataProduct_PubScope ps = dataManager.load(DataProduct_PubScope.class).one();
            lg.setPubScope(ps);

            orm.persist(lg);

            //PropertiesInList
            BaseUuidEntity[] pils = linkToTestFacadelayer(lg, ps, LG_FL_STRING);
            orm.persist(pils[0]); orm.persist(pils[1]);

            trans.commit();
        }

        return lgId;
    }

    private UUID prepareMapTestdata(){

        Persistence pers = container.persistence();
        UUID mapId = null;

        try (Transaction trans = pers.createTransaction()) {
            EntityManager orm = pers.getEntityManager();

            // Map
            Map map = container.metadata().create(Map.class);
            mapId = map.getId();

            setDprodFields(map, MAP_DP_STRING);

            DataProduct_PubScope ps = dataManager.load(DataProduct_PubScope.class).one();
            map.setPubScope(ps);

            orm.persist(map);

            //PropertiesInList
            BaseUuidEntity[] pils = linkToTestFacadelayer(map, ps, MAP_FL_STRING);
            orm.persist(pils[0]); orm.persist(pils[1]);

            trans.commit();
        }

        return mapId;
    }

    private UUID prepareFlTestdata(){

        Persistence pers = container.persistence();
        UUID flId = null;

        try (Transaction trans = pers.createTransaction()) {
            EntityManager orm = pers.getEntityManager();
            
            // FacadeLayer
            FacadeLayer fl = container.metadata().create(FacadeLayer.class);
            flId = fl.getId();

            fl.setTransparency(FL_SA_TRANSPARENCY);

            setDprodFields(fl, FL_DP_STRING);

            DataProduct_PubScope ps = dataManager.load(DataProduct_PubScope.class).one();
            fl.setPubScope(ps);

            orm.persist(fl);

            //PropertiesInFacade
            BaseUuidEntity[] pifs = linkToTestDsv(fl, ps, FL_DSV_STRING);
            orm.persist(pifs[0]); orm.persist(pifs[1]); orm.persist(pifs[2]);

            trans.commit();
        }

        return flId;
    }

    private static void setDprodFields(DataProduct dp, String ident){
        dp.setDerivedIdentifier(ident);
        dp.setIdentPart(ident);
        dp.setIdentIsPartial(false);

        dp.setThemePublication(themePubForTest);
    }

    private static BaseUuidEntity[] linkToDependencies(DataSetView ds, String dependencyName){
        Component comp = container.metadata().create(Component.class);
        comp.setName(dependencyName);

        Relation rel = container.metadata().create(Relation.class);
        rel.setRelationType(RelationType.DATA);
        rel.setDependency(comp);
        rel.setDataSetView(ds);

        return new BaseUuidEntity[]{comp, rel};
    }

    private static BaseUuidEntity[] linkToPermissions(DataSetView ds, String roleName){
        Role role = container.metadata().create(Role.class);
        role.setName(roleName);

        Permission perm = container.metadata().create(Permission.class);
        perm.setLevel(PermissionLevelEnum.READ);
        perm.setRole(role);
        perm.setDataSetView(ds);

        return new BaseUuidEntity[]{role, perm};
    }

    private static StyleAsset linkToStyleAsset(DataSetView ds, String assetFileName){
        StyleAsset sa = container.metadata().create(StyleAsset.class);
        sa.setFileContent(new byte[]{});
        sa.setFileName(assetFileName);
        sa.setDatasetSetView(ds);
        LinkedList<StyleAsset> assets = new LinkedList<>();
        assets.add(sa);
        ds.setStyleAssets(assets);

        return sa;
    }

    private static BaseUuidEntity[] linkToTestFacadelayer(ProductList pl, DataProduct_PubScope ps, String flIdentifier){
        FacadeLayer fl = container.metadata().create(FacadeLayer.class);

        setDprodFields(fl, flIdentifier);
        fl.setPubScope(ps);

        PropertiesInList pil = container.metadata().create(PropertiesInList.class);
        pil.setProductList(pl);
        pil.setSingleActor(fl);
        pil.setSort(99);

        return new BaseUuidEntity[]{fl, pil};
    }

    private static BaseUuidEntity[] linkToTestDsv(FacadeLayer fl, DataProduct_PubScope ps, String dsvIdentifier){
        
        RasterDS rds = container.metadata().create(RasterDS.class);
        rds.setPath(UUID.randomUUID().toString());
        
        RasterView dsv = container.metadata().create(RasterView.class);
        setDprodFields(dsv, dsvIdentifier);
        dsv.setPubScope(ps);
        dsv.setRasterDS(rds);

        PropertiesInFacade pif = container.metadata().create(PropertiesInFacade.class);
        pif.setFacadeLayer(fl);
        pif.setDataSetView(dsv);
        pif.setSort(99);

        return new BaseUuidEntity[]{rds, dsv, pif};
    }

    private UUID prepareTvTestdata(){

        Persistence pers = container.persistence();
        UUID tvId = null;

        try (Transaction trans = pers.createTransaction()) {
            EntityManager orm = pers.getEntityManager();

            //PostgresTable
            PostgresDB db = container.metadata().create(PostgresDB.class);
            db.setIdentifier(TV_DB_STRING);
            db.setTitle(TV_DB_STRING);
            db.setDbServiceUrl(TV_DB_STRING);

            DbSchema dt = container.metadata().create(DbSchema.class);
            dt.setSchemaName(TV_SCHEMA_STRING);
            dt.setPostgresDB(db);

            PostgresTable tbl = container.metadata().create(PostgresTable.class);
            tbl.setTableName(TV_TABLE_STRING);
            tbl.setTitle(TV_TABLE_STRING);
            tbl.setIdFieldName("fuu");
            tbl.setCatSyncStamp(LocalDateTime.now());
            tbl.setDbSchema(dt);
            tbl.setGeoFieldName(TV_TABLE_STRING);

            orm.persist(db);
            orm.persist(dt);
            orm.persist(tbl);

            // TableView
            TableView tv = container.metadata().create(TableView.class);
            tvId = tv.getId();

            tv.setSearchFacet(TV_DP_STRING);
            tv.setTransparency(TV_SA_TRANSPARENCY);
            setDprodFields(tv, TV_DP_STRING);
            tv.setPostgresTable(tbl);

            DataProduct_PubScope ps = dataManager.load(DataProduct_PubScope.class).one();
            tv.setPubScope(ps);

            orm.persist(tv);

            //Fields
            TableField tf = container.metadata().create(TableField.class);
            tf.setName(TV_DP_STRING);
            tf.setTypeName("bar");
            tf.setPostgresTable(tbl);
            tf.setAlias(TV_FIELD_ALIAS);

            ViewField vf = container.metadata().create(ViewField.class);
            vf.setSort(TV_SA_TRANSPARENCY);
            vf.setTableField(tf);
            vf.setTableView(tv);
            LinkedList<ViewField> fieldList = new LinkedList<>();
            fieldList.add(vf);
            tv.setViewFields(fieldList);

            orm.persist(tf);
            orm.persist(vf);

            //Assets
            StyleAsset sa = linkToStyleAsset(tv, TV_DP_STRING);
            orm.persist(sa);

            //Permissions
            BaseUuidEntity[] perm = linkToPermissions(tv, TV_ROLE_STRING);
            orm.persist(perm[0]); orm.persist(perm[1]);

            //Dependencies
            BaseUuidEntity[] dep = linkToDependencies(tv, TV_COMPONENT_STRING);
            orm.persist(dep[0]); orm.persist(dep[1]);

            trans.commit();
        }

        return tvId;
    }
}