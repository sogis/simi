package ch.so.agi.simi.core.morph;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.core.copy.CopyService;
import ch.so.agi.simi.entity.data.*;
import ch.so.agi.simi.entity.data.datasetview.*;
import ch.so.agi.simi.entity.extended.Component;
import ch.so.agi.simi.entity.extended.Relation;
import ch.so.agi.simi.entity.extended.RelationType;
import ch.so.agi.simi.entity.iam.Permission;
import ch.so.agi.simi.entity.iam.PermissionLevelEnum;
import ch.so.agi.simi.entity.iam.Role;
import ch.so.agi.simi.entity.product.*;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ch.so.agi.simi.core.copy.CopyServiceBean.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
// See https://doc.cuba-platform.com/manual-7.2/integration_tests_mw.html

class MorphProductServiceBeanTest {

    private static final String IDENT_PREFIX_ALLTESTS = "morphtest.";
    private static final String IDENT_PREFIX_SINGLETEST = IDENT_PREFIX_ALLTESTS + "single.";

    private static final String LINK_DS_IDENT = IDENT_PREFIX_ALLTESTS + "link_ds";
    private static final String FL_IDENT = IDENT_PREFIX_SINGLETEST + "facade";
    private static final String LG_IDENT = IDENT_PREFIX_SINGLETEST + "group";
    private static final String MAP_IDENT = IDENT_PREFIX_SINGLETEST + "map";


    @RegisterExtension
    static TestContainer container = SimiTestContainer.Common.INSTANCE;

    static DataManager dataManager;
    static MorphProductService serviceBean;

    static DataSetView linkDs;
    static DataProduct_PubScope deletable;


    @BeforeAll
    static void beforeAll() {

        dataManager = AppBeans.get(DataManager.class);
        serviceBean = AppBeans.get(MorphProductService.class);

        removeMorphTestEntities(false); // Falls von unsauberen vorhergehendem run übriggebliebene existieren ...

        deletable = dataManager.load(DataProduct_PubScope.class).id(UUID.fromString("55bdf0dd-d997-c537-f95b-7e641dc515df")).one();

        linkDs = new DataSetView();
        linkDs.setIdentifier(LINK_DS_IDENT);
        linkDs.setPubScope(deletable);

        dataManager.commit(linkDs);
    }

    @AfterAll
    static void afterAll() {
        removeMorphTestEntities(false);
    }

    private static void removeMorphTestEntities(boolean singleTestEntitiesOnly){

        String identPrefix = IDENT_PREFIX_SINGLETEST;

        if(!singleTestEntitiesOnly)
            identPrefix = IDENT_PREFIX_ALLTESTS;

        List<DataProduct> testEntities = dataManager.load(DataProduct.class)
                .query("e.identifier like :identifier")
                .parameter("identifier", identPrefix + "%")
                .list();

        if(testEntities == null || testEntities.size() == 0)
            return;

        CommitContext trans = new CommitContext();

        for(DataProduct dp : testEntities)
            trans.addInstanceToRemove(dp);

        dataManager.commit(trans);
    }

    @Test
    void morphFlToGroup_OK(){
        UUID flId = createTestFl();

        UUID morphedId = serviceBean.morphProduct(flId, ProdType.FACADELAYER, ProdType.LAYERGROUP);
        assertValidResult(morphedId, ProdType.LAYERGROUP);

        removeMorphTestEntities(true);
    }

    @Test
    void morphFlToMap_OK(){
        UUID flId = createTestFl();

        UUID morphedId = serviceBean.morphProduct(flId, ProdType.FACADELAYER, ProdType.MAP);
        assertValidResult(morphedId, ProdType.MAP);

        removeMorphTestEntities(true);
    }

    @Test
    void morphLgToFl_OK(){
        UUID lgId = createTestLg();

        UUID morphedId = serviceBean.morphProduct(lgId, ProdType.LAYERGROUP, ProdType.FACADELAYER);
        assertValidResult(morphedId, ProdType.FACADELAYER);

        removeMorphTestEntities(true);
    }

    @Test
    void morphLgToMap_OK(){
        UUID lgId = createTestLg();

        UUID morphedId = serviceBean.morphProduct(lgId, ProdType.LAYERGROUP, ProdType.MAP);
        assertValidResult(morphedId, ProdType.MAP);

        removeMorphTestEntities(true);
    }

    @Test
    void morphMapToFl_OK(){
        UUID mapId = createTestMap();

        UUID morphedId = serviceBean.morphProduct(mapId, ProdType.MAP, ProdType.LAYERGROUP);
        assertValidResult(morphedId, ProdType.LAYERGROUP);

        removeMorphTestEntities(true);
    }

    @Test
    void morphMapToLg_OK(){
        UUID mapId = createTestMap();

        UUID morphedId = serviceBean.morphProduct(mapId, ProdType.MAP, ProdType.LAYERGROUP);
        assertValidResult(morphedId, ProdType.LAYERGROUP);

        removeMorphTestEntities(true);
    }

    private void assertValidResult(UUID morphedID, ProdType morphedType){
        if(morphedType == ProdType.FACADELAYER){
            FacadeLayer dp = dataManager.load(FacadeLayer.class).id(morphedID).view(MorphProductServiceBean.VIEWNAME_FACADE).one();
            Assertions.assertEquals(1, dp.getDataSetViews().size(), "Facadelayer must have one linked DatasetView");
        }
        else if(morphedType == ProdType.LAYERGROUP){
            LayerGroup dp = dataManager.load(LayerGroup.class).id(morphedID).view(MorphProductServiceBean.VIEWNAME_GROUP).one();
            Assertions.assertEquals(1, dp.getSingleActors().size(), "LayerGroup must have one linked DatasetView");
        }
        else if(morphedType == ProdType.MAP){
            Map dp = dataManager.load(Map.class).id(morphedID).view(MorphProductServiceBean.VIEWNAME_MAP).one();
            Assertions.assertEquals(1, dp.getSingleActors().size(), "Map must have one linked DatasetView");
        }
        else {
            throw new RuntimeException("Unhandled Dprod Type");
        }
    }


    private UUID createTestFl(){
        FacadeLayer fl = new FacadeLayer();
        fl.setIdentifier(FL_IDENT);
        fl.setPubScope(deletable);

        PropertiesInFacade pif = new PropertiesInFacade();
        pif.setDataSetView(loadLinkDs());
        pif.setFacadeLayer(fl);
        pif.setSort(1);

        LinkedList<PropertiesInFacade> pifList = new LinkedList<>();
        pifList.add(pif);
        fl.setDataSetViews(pifList);

        CommitContext trans = new CommitContext();
        trans.addInstanceToCommit(fl);
        trans.addInstanceToCommit(pif);

        dataManager.commit(trans);

        return fl.getId();
    }

    private static LinkedList<PropertiesInList> createListWithOnePil(ProductList pl) {
        PropertiesInList pil = new PropertiesInList();
        pil.setSingleActor(loadLinkDs());
        pil.setProductList(pl);
        pil.setSort(1);

        LinkedList<PropertiesInList> pilList = new LinkedList<>();
        pilList.add(pil);
        pl.setSingleActors(pilList);

        return pilList;
    }

    private UUID createTestLg(){
        LayerGroup lg = new LayerGroup();
        lg.setIdentifier(LG_IDENT);
        lg.setPubScope(deletable);

        LinkedList<PropertiesInList> pilList = createListWithOnePil(lg);
        lg.setSingleActors(pilList);

        CommitContext trans = new CommitContext();
        trans.addInstanceToCommit(lg);
        trans.addInstanceToCommit(pilList.getFirst());

        dataManager.commit(trans);

        return lg.getId();
    }

    private UUID createTestMap(){
        Map map = new Map();
        map.setIdentifier(MAP_IDENT);
        map.setPubScope(deletable);

        LinkedList<PropertiesInList> pilList = createListWithOnePil(map);
        map.setSingleActors(pilList);

        CommitContext trans = new CommitContext();
        trans.addInstanceToCommit(map);
        trans.addInstanceToCommit(pilList.getFirst());

        dataManager.commit(trans);

        return map.getId();
    }

    private static DataSetView loadLinkDs(){
        return dataManager.load(DataSetView.class)
                .query("e.identifier = :identifier")
                .parameter("identifier", LINK_DS_IDENT)
                .one();
    }

}