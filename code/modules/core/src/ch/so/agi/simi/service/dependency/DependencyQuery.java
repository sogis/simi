package ch.so.agi.simi.service.dependency;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.global.Resources;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class DependencyQuery {

    public static List<DependencyInfo> execute(UUID dataSetId, Persistence persistence, Resources resources){

        List<DependencyInfo> result = null;

        String queryString = loadQuery(resources);
        QueryRunner runner = new QueryRunner(persistence.getDataSource());

        try {
            result = runner.query(queryString, dataSetId, rs -> {
                List<DependencyInfo> rows = new LinkedList<>();

                while (rs.next()) {

                    DependencyInfo di = new DependencyInfo();
                    di.setObjectType(rs.getString("dep_typ"));
                    di.setObjectName(rs.getString("dep_name"));
                    di.setDependencyInfo(rs.getString("dep_relation"));

                    rows.add(di);
                }
                return rows;
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;


/*        List<DependencyInfo> res = null;

        try (Transaction tx = persistence.createTransaction()) {

            EntityManager em = persistence.getEntityManager();

            Query query = em.createNativeQuery(queryString);
            query.setParameter(1, "'" + dataSetId + "'");

            List resObj = query.getResultList();
            res = mapToDependencyInfo(resObj);

            tx.commit();
        }

        return res;*/
    }

    private static List<DependencyInfo> mapToDependencyInfo(List resultObjects){
        if(resultObjects == null)
            return null;

        List<DependencyInfo> list = new LinkedList<>();

        for(Object rowObj : resultObjects){
            String[] values = (String[])rowObj;

            DependencyInfo di = new DependencyInfo();
            di.setObjectType(values[0]);
            di.setObjectName(values[1]);
            di.setDependencyInfo(values[2]);

            list.add(di);
        }

        return list;
    }

    private static String loadQuery(Resources resources){
/*        String queryString = resources.getResourceAsString("ds_dependencies.sql");
        if (queryString == null)
            throw new RuntimeException("Could not load sql query file from config folder");*/



        return query;
    }

    private static final String query =
            "/*\n" +
                    " * CTE's sammeln die Informationen zu den Abhängigkeiten auf ein Dataset oder auf eine DataSetView \n" +
                    " * des Dataset. Indirekte Abhängigkeiten werden nicht ausgegeben. Beispiel: \n" +
                    " * Von der Kette DSV --> Fassade --> Gruppe wird nur die direkte Beziehung DSV --> Fassade ausgegeben\n" +
                    " */\n" +
                    "\n" +
                    "WITH\n" +
                    "\n" +
                    "-- Shared ********************************************************************\n" +
                    "\n" +
                    "ds_dsv AS ( -- datasetviews für ein dataset\n" +
                    "\tSELECT \n" +
                    "\t\tCOALESCE(rv.raster_ds_id, tv.postgres_table_id) AS ds_id,\n" +
                    "\t\tdsv.id AS dsv_id,\n" +
                    "\t\t'View' AS typ,\n" +
                    "\t\t1 AS sort\n" +
                    "\tFROM \n" +
                    "\t\tsimiproduct_data_set_view dsv\n" +
                    "\tLEFT JOIN \n" +
                    "\t\tsimidata_raster_view rv ON dsv.id = rv.id\n" +
                    "\tLEFT JOIN \n" +
                    "\t\tsimidata_table_view tv ON dsv.id = tv.id\n" +
                    "),\n" +
                    "\n" +
                    "-- DATENPRODUKTE ********************************************************************\n" +
                    "\n" +
                    "ds_fl AS ( -- facadelayer für eine datasetview\n" +
                    "\tSELECT \n" +
                    "\t\tds_id,\n" +
                    "\t\tdsv_id,\n" +
                    "\t\tpif.facade_layer_id AS fl_id,\n" +
                    "\t\t'Fassade' AS typ,\n" +
                    "\t\t2 AS sort\n" +
                    "\tFROM \n" +
                    "\t\tds_dsv\n" +
                    "\tJOIN \n" +
                    "\t\tsimiproduct_properties_in_facade pif ON dsv_id = pif.data_set_view_id \n" +
                    "\tWHERE \n" +
                    "\t\tpif.delete_ts IS NULL \n" +
                    "),\n" +
                    "\n" +
                    "ds_pl AS ( -- productlist für eine datasetview. Productlist = Karte oder Layergruppe\n" +
                    "\tSELECT \n" +
                    "\t\tds_id,\n" +
                    "\t\tdsv_id,\n" +
                    "\t\tpil.product_list_id AS pl_id,\n" +
                    "\t\t--(_map.id IS NOT NULL) AS is_map,\n" +
                    "\t\tCASE\n" +
                    "\t\t\tWHEN _map.id IS NULL THEN 'Gruppe'\n" +
                    "\t\t\tELSE 'Karte'\n" +
                    "\t\tEND AS typ,\n" +
                    "\t\tCASE\n" +
                    "\t\t\tWHEN _map.id IS NULL THEN 3\n" +
                    "\t\t\tELSE 4\n" +
                    "\t\tEND AS sort\n" +
                    "\tFROM \n" +
                    "\t\tds_dsv\n" +
                    "\tJOIN \n" +
                    "\t\tsimiproduct_properties_in_list pil ON dsv_id = pil.single_actor_id \n" +
                    "\tLEFT JOIN \n" +
                    "\t\tsimiproduct_map _map ON pil.product_list_id = _map.id \n" +
                    "\tWHERE \n" +
                    "\t\tpil.delete_ts IS NULL \n" +
                    "),\n" +
                    "\n" +
                    "dp_union AS (\n" +
                    "\tSELECT ds_id, dsv_id, dsv_id AS dp_id, typ, sort FROM ds_dsv\n" +
                    "\tUNION ALL\n" +
                    "\tSELECT ds_id, dsv_id, fl_id AS dp_id, typ, sort FROM ds_fl\n" +
                    "\tUNION ALL\n" +
                    "\tSELECT ds_id, dsv_id, pl_id AS dp_id, typ, sort FROM ds_pl\n" +
                    "),\n" +
                    "\n" +
                    "dataprod_cols AS ( -- notwendige informationen aller ungelöschten Datenprodukte\n" +
                    "\tSELECT \n" +
                    "\t\tid,\n" +
                    "\t\ttitle,\n" +
                    "\t\tidentifier\n" +
                    "\tFROM \n" +
                    "\t\tsimiproduct_data_product dp\n" +
                    "\tWHERE\n" +
                    "\t\tdp.delete_ts IS NULL\n" +
                    "),\n" +
                    "\n" +
                    "dataprod AS (\n" +
                    "\tSELECT\n" +
                    "\t\tds_id,\n" +
                    "\t\tconcat(dp_union.typ, ' (Produkt)') AS dep_typ,\n" +
                    "\t\tconcat(dp.identifier, ' | ', dp.title) AS dep_name,\n" +
                    "\t\tCASE \n" +
                    "\t\t\tWHEN dsv_id = dp_id THEN 'Ist View des Dataset'\n" +
                    "\t\t\tELSE concat('Dataset ist via View \"', dsv.identifier, '\" in ', dp_union.typ, ' enthalten.')\n" +
                    "\t\tEND AS dep_relation,\n" +
                    "\t\tsort AS subsort,\n" +
                    "\t\t1 AS sort\n" +
                    "\tFROM \n" +
                    "\t\tdp_union\n" +
                    "\tJOIN \n" +
                    "\t\tdataprod_cols dp ON dp_union.dp_id = dp.id\n" +
                    "\tJOIN \n" +
                    "\t\tdataprod_cols dsv ON dp_union.dsv_id = dsv.id\n" +
                    "),\n" +
                    "\n" +
                    "-- Dependencies ********************************************************************\n" +
                    "\n" +
                    "ds_dsv_relation AS ( -- Alle Beziehungen eines DS via DSV auf eine Dependency\n" +
                    "\tSELECT \n" +
                    "\t\tds_id,\n" +
                    "\t\tdsv_id,\n" +
                    "\t\tdp.identifier AS dsv_identifier,\n" +
                    "\t\trel.dependency_id AS dep_id\n" +
                    "\tFROM \n" +
                    "\t\tds_dsv\n" +
                    "\tJOIN \n" +
                    "\t\tsimidependency_relation rel ON ds_dsv.dsv_id = rel.data_set_view_id\n" +
                    "\tJOIN \n" +
                    "\t\tsimiproduct_data_product dp ON ds_dsv.dsv_id = dp.id \n" +
                    "\tWHERE \n" +
                    "\t\t\trel.delete_ts IS NULL \n" +
                    "\t\tAND \n" +
                    "\t\t\tdp.delete_ts IS NULL\n" +
                    "\t\tAND \n" +
                    "\t\t\trel.relation_type NOT LIKE '%display%' -- \"Verzeihender\" Filter, falls die Nachführung vergessen geht...\n" +
                    "),\n" +
                    "\n" +
                    "dependency_extprop AS (\n" +
                    "\tSELECT \n" +
                    "\t\t* \n" +
                    "\tFROM (\n" +
                    "\t\tVALUES \n" +
                    "\t\t\t('simiDependency_Component', 'Modul', 1),\n" +
                    "\t\t\t('simiDependency_FeatureInfo', 'Spez. Featureinfo', 3),\n" +
                    "\t\t\t('simiDependency_CCCIntegration', 'CCC-Integration', 6),\n" +
                    "\t\t\t('simiDependency_Report', 'Report', 9)\n" +
                    "\t) \n" +
                    "\tAS t (dtype, typename, sort)\n" +
                    "),\n" +
                    "\n" +
                    "dependency AS (\n" +
                    "\tSELECT \n" +
                    "\t\tds_id,\n" +
                    "\t\t\"name\" AS dep_name,\n" +
                    "\t\tconcat(typename, ' (Abhängigkeit)') AS dep_typ,\n" +
                    "\t\tconcat('Dataset ist via View \"', rel.dsv_identifier, '\" in ', typename, ' enthalten.') AS dep_relation,\n" +
                    "\t\tsort AS subsort,\n" +
                    "\t\t2 AS sort\n" +
                    "\tFROM \n" +
                    "\t\tsimidependency_dependency dep\n" +
                    "\tJOIN\n" +
                    "\t\tdependency_extprop ep ON dep.dtype = ep.dtype\n" +
                    "\tJOIN \n" +
                    "\t\tds_dsv_relation rel ON dep.id = rel.dep_id\n" +
                    "\tWHERE \n" +
                    "\t\tdep.delete_ts IS NULL \n" +
                    "),\n" +
                    "\n" +
                    "-- ***********************************************************************************\n" +
                    "\n" +
                    "all_dependencies AS (\n" +
                    "\tSELECT * FROM (\n" +
                    "\t\tSELECT ds_id, dep_typ, dep_name, dep_relation, subsort, sort FROM dataprod\n" +
                    "\t\tUNION ALL \n" +
                    "\t\tSELECT ds_id, dep_typ, dep_name, dep_relation, subsort, sort FROM dependency\n" +
                    "\t) sub\n" +
                    "\tORDER BY \n" +
                    "\t\tsort, \n" +
                    "\t\tsubsort,\n" +
                    "\t\tdep_name\n" +
                    ")\n" +
                    "\n" +
                    "SELECT dep_typ, dep_name, dep_relation FROM all_dependencies WHERE ds_id = ? --'30903fd9-2d3e-4e12-99a5-3f1e038b5087'\n";
}
