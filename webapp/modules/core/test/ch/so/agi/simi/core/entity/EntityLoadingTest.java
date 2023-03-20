package ch.so.agi.simi.core.entity;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.entity.data.*;
import ch.so.agi.simi.entity.extended.*;
import ch.so.agi.simi.entity.iam.Group;
import ch.so.agi.simi.entity.iam.Role;
import ch.so.agi.simi.entity.iam.User;
import ch.so.agi.simi.entity.data.datasetview.DataSetView;
import ch.so.agi.simi.entity.data.datasetview.RasterView;
import ch.so.agi.simi.entity.data.datasetview.TableView;
import ch.so.agi.simi.entity.data.datasetview.ViewField;
import ch.so.agi.simi.entity.product.*;
import ch.so.agi.simi.entity.theme.CustomFileType;
import ch.so.agi.simi.entity.theme.Theme;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.entity.theme.dbview.ThemePubValidation;
import ch.so.agi.simi.entity.theme.org.Agency;
import ch.so.agi.simi.entity.theme.org.OrgUnit;
import ch.so.agi.simi.entity.theme.org.SubOrg;
import ch.so.agi.simi.entity.theme.subarea.PublishedSubArea;
import ch.so.agi.simi.entity.theme.subarea.SubArea;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
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
    void postgresDb_OK() {
        loadEntityViews(
                PostgresDB.class,
                new String[]{
                    "postgresDB-browse",
                    "postgresDB-edit"
                });
    }

    @Test
    void dbSchema_OK() {
        loadEntityViews(
                DbSchema.class,
                new String[]{
                        "dbSchema-browse",
                        "dbSchema-edit",
                        "postgresTable-edit-dbSchemas"
                });
    }

    @Test
    void postgresTable_OK() {
        loadEntityViews(
                PostgresTable.class,
                new String[]{
                        "postgresTable-browse",
                        "postgresTable-edit",
                        "tableView-edit-postgresTables",
                        "dependencyList-postgresTable"
                });
    }

    @Test
    void tableField_OK() {
        loadEntityViews(
                TableField.class,
                new String[]{
                        "tableField-edit",
                        "tableView-edit-tableFields",
                        "viewField-edit-tableFields"
                });
    }

    @Test
    void group_OK() {
        loadEntityViews(
                Group.class,
                new String[]{
                        "group-browse",
                        "group-edit"
                });
    }

    @Test
    void user_OK() {
        loadEntityViews(
                User.class,
                new String[]{
                        "user-browse",
                        "user-edit"
                });
    }

    @Test
    void role_OK() {
        loadEntityViews(
                Role.class,
                new String[]{
                        "role-browse",
                        "role-edit",
                        "tableView-edit-roles"
                });
    }

    @Test
    void dependency_OK() {
        loadEntityView(
                Dependency.class,
                "dependency-browse"
        );
    }

    @Test
    void ccc_OK() {
        loadEntityView(
                CCCIntegration.class,
                "ccc-edit"
        );
    }

    @Test
    void component_OK() {
        loadEntityView(
                Component.class,
                "component-edit"
                );
    }

    @Test
    void featureInfo_OK() {
        loadEntityView(
                FeatureInfo.class,
               "featureInfo-edit"
                );
    }

    @Test
    void relation_OK() {
        loadEntityView(
                Relation.class,
                "relation-edit"
        );
    }

    @Test
    void report_OK() {
        loadEntityView(
                Report.class,
                "report-edit"
        );
    }

    @Test
    void dataProduct_OK() {
        loadEntityView(
                DataProduct.class,
                "dataProduct-browse"
        );
    }

    @Test
    void dataProduct_PubScope_OK() {
        loadEntityView(
                DataProduct_PubScope.class,
                "dataProduct-pubScope-fragment"
        );
    }

    @Test
    void facadeLayer_OK() {
        loadEntityView(
                FacadeLayer.class,
                "facadeLayer-edit"
        );
    }

    @Test
    void dataSetView_OK() {
        loadEntityView(
                DataSetView.class,
                "facadeLayer-edit-dataSetViews"
        );
    }

    @Test
    void singleActor_OK() {
        loadEntityViews(
                SingleActor.class,
                new String[]{
                        "map-edit-singleActors",
                        "layerGroup-edit-singleActors"
                });
    }

    @Test
    void layerGroup_OK() {
        loadEntityView(
                LayerGroup.class,
                "layerGroup-edit"
        );
    }

    @Test
    void map_OK() {
        loadEntityView(
                Map.class,
                "map-edit"
        );
    }

    @Test
    void rasterDs_OK() {
        loadEntityViews(
                RasterDS.class,
                new String[]{
                        "rasterDs-browse",
                        "rasterDs-edit",
                        "rasterView-edit-rasters"
                });
    }

    @Test
    void rasterView_OK() {
        loadEntityView(
                RasterView.class,
                "rasterView-edit"
        );
    }

    @Test
    void tableView_OK() {
        loadEntityView(
                TableView.class,
                "tableView-edit"
        );
    }

    @Test
    void viewField_OK() {
        loadEntityView(
                ViewField.class,
                "viewField-edit"
        );
    }

    @Test
    void externalWmsService_OK() {
        loadEntityViews(
                ExternalWmsService.class,
                new String[]{
                        "externalWmsService-edit",
                        "externalWmsService-browse",
                        "externalWmsLayers-edit-services"
                }
        );
    }

    @Test
    void externalMapLayer_OK() {
        loadEntityView(
                ExternalWmsLayer.class,
                "externalWmsLayer-edit"
        );
    }


    @Test
    void theme_OrgUnit_OK(){
        loadEntityViews(
            OrgUnit.class,
            new String[]{
                "theme-edit-dataOwners",
                "orgUnit-browse"
            }
        );
    }

    @Test
    void theme_Agency_OK(){
        loadEntityViews(
            Agency.class,
            new String[]{"agency-edit",
                    "subOrg-agency-lookup"}
        );
    }

    @Test
    void theme_SubOrg_OK(){
        loadEntityView(
                SubOrg.class,
                "subOrg-edit"
        );
    }

    @Test
    void theme_Theme_OK(){
        loadEntityViews(
            Theme.class,
            new String[]{
                "theme-browse",
                "theme-edit",
                "themePublication-edit-theme"
            }
        );
    }

    @Test
    void theme_ThemePublication_OK(){
        loadEntityViews(
                ThemePublication.class,
                new String[]{
                        "themePublication-edit",
                        "dProdEditors-themePub-lookup",
                        "themePubDataSheet-edit"
                }
        );
    }

    @Test
    void theme_ThemePubValidation_OK(){
        loadEntityViews(
                ThemePubValidation.class,
                new String[]{
                        "themePubValidation-edit"
                }
        );
    }

    @Test
    void theme_CustomFileType_OK(){
        loadEntityView(
                CustomFileType.class,
                "customFileType-browse-edit");
    }

    @Test
    void theme_DataProductDsv_OK(){
        loadEntityView(
                DataProductDsv.class,
                "themePublication-edit-product");
    }

    @Test
    void theme_PubSubArea_OK(){
        loadEntityViews(
                PublishedSubArea.class,
                new String[]{
                        "publishedSubArea-edit"
                }
        );
    }

    @Test
    void theme_SubArea_OK(){
        loadEntityViews(
                SubArea.class,
                new String[]{
                        "subArea-browse"
                }
        );
    }

    private void loadEntityViews(Class entityClass, String[] viewNames){

        ArrayList<ViewLoadException> ex = new ArrayList<>();

        for(String viewName : viewNames){
            try {
                loadEntityView(entityClass, viewName);
            }
            catch(Exception e){
                ex.add(new ViewLoadException(viewName, e));
            }
        }

        if(ex.size() > 0)
            throw new EntityLoadException(ex);
    }

    private void loadEntityView(Class entityClass, String viewName){

        String entityName = null;
        try{
            Field nameField = entityClass.getField("NAME");
            if(nameField == null)
                throw new RuntimeException("Entity class has no NAME field declared");

            entityName = nameField.get(null).toString();
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }

        LoadContext context = LoadContext.create(entityClass)
                .setQuery(queryFor(entityName))
                .setView(viewName);

        dataManager.loadList(context);
    }

    private static LoadContext.Query queryFor(String entityName){
        String queryStr = MessageFormat.format("select e from {0} e", entityName);
        return LoadContext.createQuery(queryStr);
    }
}