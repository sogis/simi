package ch.so.agi.simi.service.dependency;

import ch.so.agi.simi.entity.data.PostgresTable;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.filter.Condition;

import java.util.Optional;
import java.util.UUID;

public class TableNameQuery {

    public static String[] execute(UUID dataSetId, DataManager dbClient){

        String query = "select e from simiData_PostgresTable e where e.id = :dataSetId";

        Optional<PostgresTable> tableInfo = dbClient.load(PostgresTable.class)
                .query(query)
                .parameter("dataSetId", dataSetId)
                .view("dependencyService-fullTableName")
                .optional();

        if (!tableInfo.isPresent())
            throw new TableNotFoundException("No table found with id " + dataSetId);

        return new String[]{
                tableInfo.get().getModelSchema().getSchemaName(),
                tableInfo.get().getTableName()
        };
    }
}