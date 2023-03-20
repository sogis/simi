package ch.so.agi.simi.core.dependency.simi;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.entity.dependency.*;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.testsupport.TestContainer;
import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

class SimiSearchTest {

    public static Logger log = LoggerFactory.getLogger(SimiSearchTest.class);

    @RegisterExtension
    static TestContainer container = SimiTestContainer.Common.INSTANCE;

    static DataManager dataManager;
    static SimiSearch simiSearch;
    
    @BeforeAll
    static void beforeAll() {
        dataManager = AppBeans.get(DataManager.class);
        
        simiSearch = new SimiSearch(dataManager);
        simiSearch._setDbEntityClass(DependencyForTest.class);

        dropTestTable();
        createTestTableWithData();
    }

    @AfterAll
    static void afterAll() {
        dropTestTable();
    }

    @Test
    public void multiTbl_OK(){

        List<UUID> ids = loadIdsByIdents(List.of("table.10", "table.11"));

        List<DependencyBase> res = simiSearch.loadSimiDependencies(ids);

        Assertions.assertEquals(4, res.size(), "expected list with 2 elems but got list " + res.toString());
    }

    @Test
    public void multiTbl_Root_OK(){

        List<UUID> ids = loadIdsByIdents(List.of("table.10", "table.11"));

        List<DependencyBase> res = simiSearch.loadSimiDependencies(ids);

        List<DependencyBase> filtered = res.stream().filter(dep -> dep.getObjName().equals(dep.getRootObjName())).collect(Collectors.toList()); // filter true for the 2 root obj

        Assertions.assertEquals(2, filtered.size(), "expected list with 2 elems but got list " + filtered.toString());
    }

    @Test
    public void isoTbl_NoDownstream_OK(){

        UUID uid = loadIdByName("table.11");

        List<DependencyBase> res = simiSearch.loadSimiDependencies(List.of(uid));

        Assertions.assertEquals(1, res.size());
    }

    @Test
    public void linkedTbl_MultiDownstream_OK(){

        UUID uid = loadIdByName("table.10");

        List<DependencyBase> res = simiSearch.loadSimiDependencies(List.of(uid));

        Assertions.assertEquals(3, res.size(), "expected list with 2 elems but got list " + res.toString());
    }

    @Test
    public void isoDsv_NoDownstream_OK(){

        UUID uid = loadIdByName("dsv_ras.21");

        List<DependencyBase> res = simiSearch.loadSimiDependencies(List.of(uid));

        Assertions.assertEquals(1, res.size());
    }

    @Test
    public void linkedDsv_MultiDownstream_OK(){

        UUID uid = loadIdByName("dsv_ext.20");

        List<DependencyBase> res = simiSearch.loadSimiDependencies(List.of(uid));

        Assertions.assertEquals(10, res.size(), "expected list with 9 elems but got list: " + res.toString());
    }

    @Test
    public void linkedDsv_RootPropagation_OK(){

        UUID uid = loadIdByName("dsv_ext.20");

        List<DependencyBase> res = simiSearch.loadSimiDependencies(List.of(uid));

        List<DependencyBase> filtered = res.stream() // root has equal obj and root name. filter must yield 10 (total) - 1 (root) = 9
                .filter(dep -> dep.getRootObjName() != null)
                .filter(dep -> !(dep.getObjName().equals(dep.getRootObjName())))
                .collect(Collectors.toList());

        Assertions.assertEquals(9, filtered.size(), "expected list with 9 elems but got list: " + res.toString());
    }

    @Test
    public void isoDsvPlus1_NoDownstream_OK(){

        UUID uid = loadIdByName("feat_info.31");

        List<DependencyBase> res = simiSearch.loadSimiDependencies(List.of(uid));

        Assertions.assertEquals(1, res.size());
    }

    @Test
    public void linkedDsvPlus1_MultiDownstream_OK(){

        UUID uid = loadIdByName("fassade.30");

        List<DependencyBase> res = simiSearch.loadSimiDependencies(List.of(uid));

        Assertions.assertEquals(3, res.size(), "expected list with 2 elems but got list " + res.toString());
    }

    private UUID loadIdByName(String ident){
        return loadIdsByIdents(List.of(ident)).get(0);
    }

    private List<UUID> loadIdsByIdents(List<String> names){

        List<? extends DependencyBase> d = dataManager.load(DependencyForTest.class)
                .query("select d from simiTest_Dependency d where d.objName in :names")
                .parameter("names", names)
                .list();

        if(d.size() == 0)
            throw new RuntimeException("Query on names " + names + " must have result rows, but has none");

        return d.stream().map(obj -> obj.getObjId()).collect(Collectors.toList());
    }

    private static void dropTestTable() {
        String drop = "DROP TABLE IF EXISTS simi.simitest_dependency;";
        execDbStatement(drop);
    }

    private static void createTestTableWithData() {
        String create = "CREATE TABLE simi.simitest_dependency (\n" +
                "\tid int4 NOT NULL,\n" +
                "\tobj_name text NOT NULL,\n" +
                "\tlevel_num int4 NOT NULL,\n" +
                "\ttype_name text NOT NULL,\n" +
                "\tupstream_id uuid NULL,\n" +
                "\tobj_id uuid NOT NULL,\n" +
                "\tqual_table_name text NULL,\n" +
                "\tCONSTRAINT simitest_dependency_pkey PRIMARY KEY (id)\n" +
                ");";

        String insert = "INSERT INTO simi.simitest_dependency (id,obj_name,level_num,type_name,upstream_id,obj_id,qual_table_name) VALUES\n" +
                "\t (23,'dsv_search.23',2,'View (Suche)','b97f0f57-9e8c-49d5-900a-9a738030ae4d','20c43fe7-acc1-4454-8612-0ae55e22f4c3',NULL),\n" +
                "\t (22,'dsv_vec.22',2,'View (Vector)','b97f0f57-9e8c-49d5-900a-9a738030ae4d','a14090de-25e3-4d1c-9913-d5ed90f9b5c0',NULL),\n" +
                "\t (21,'dsv_ras.21',2,'View (Raster)',NULL,'572a9e21-0852-42ff-a73e-f883b724e1b9',NULL),\n" +
                "\t (20,'dsv_ext.20',2,'Ext. WMS',NULL,'6cd59ed6-884a-49f2-9475-bed5841dc1d0',NULL),\n" +
                "\t (36,'map.36',4,'Karte','6cd59ed6-884a-49f2-9475-bed5841dc1d0','d70a57b6-de66-4f04-b260-c71b93b4e386',NULL),\n" +
                "\t (35,'group.35',4,'Gruppe','6cd59ed6-884a-49f2-9475-bed5841dc1d0','acecfa5a-7ff5-4f74-84b5-69e01990b5f5',NULL),\n" +
                "\t (34,'module.34',3,'Modul','6cd59ed6-884a-49f2-9475-bed5841dc1d0','61445a74-2a68-4201-9496-4444ee26df7e',NULL),\n" +
                "\t (33,'report.33',3,'Report','6cd59ed6-884a-49f2-9475-bed5841dc1d0','9dc4f532-0a6f-4e0f-a683-15377aeaab78',NULL),\n" +
                "\t (32,'ccc.32',3,'CCC-Integration','6cd59ed6-884a-49f2-9475-bed5841dc1d0','24d7a9b4-507e-42d6-b503-e7adead4f40c',NULL),\n" +
                "\t (31,'feat_info.31',3,'Spez. Featureinfo','6cd59ed6-884a-49f2-9475-bed5841dc1d0','fbbf6a1c-eb71-4372-a169-318c04622504',NULL),\n" +
                "\t (30,'fassade.30',3,'Fassade','6cd59ed6-884a-49f2-9475-bed5841dc1d0','249da4f6-ad3a-4347-a950-d1293de7276d',NULL),\n" +
                "\t (41,'map.41',4,'Karte','249da4f6-ad3a-4347-a950-d1293de7276d','d9416694-6a1e-4be5-8b34-d9f5f0b51c52',NULL),\n" +
                "\t (40,'group.40',4,'Gruppe','249da4f6-ad3a-4347-a950-d1293de7276d','e5a71030-7f36-43ba-97d5-d1923425d1e4',NULL),\n" +
                "\t (11,'table.11',1,'Tabelle',NULL,'8a8f12ae-57d8-4d5f-9333-c50f89789976','table.11'),\n" +
                "\t (10,'table.10',1,'Tabelle',NULL,'b97f0f57-9e8c-49d5-900a-9a738030ae4d','table.10');\n";

        execDbStatement(String.join("\n", create, insert));
    }

    private static void execDbStatement(String statement){
        try(Transaction t = container.persistence().createTransaction()){
            Query q = container.entityManager().createNativeQuery(statement);
            q.executeUpdate();

            t.commit();
        }
    }
}