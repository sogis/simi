package ch.so.agi.simi.core.dependency.v1;

import ch.so.agi.simi.core.dependency.DependencyInfo;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.global.Resources;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class DependencyQuery {

    public static List<DependencyInfo> execute(UUID dataSetId, Persistence persistence, Resources resources) {

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
    }

    private static List<DependencyInfo> mapToDependencyInfo(List resultObjects) {
        if (resultObjects == null)
            return null;

        List<DependencyInfo> list = new LinkedList<>();

        for (Object rowObj : resultObjects) {
            String[] values = (String[]) rowObj;

            DependencyInfo di = new DependencyInfo();
            di.setObjectType(values[0]);
            di.setObjectName(values[1]);
            di.setDependencyInfo(values[2]);

            list.add(di);
        }

        return list;
    }

    private static String loadQuery(Resources resources) {

        String queryString = resources.getResourceAsString("ch/so/agi/simi/core/dependency/resources/ds_dependencies.sql");
        if (queryString == null)
            throw new RuntimeException("Could not load sql query file from config folder");

        return queryString;
    }
}
