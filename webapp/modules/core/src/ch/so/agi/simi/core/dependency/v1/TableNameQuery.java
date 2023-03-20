package ch.so.agi.simi.core.dependency.v1;

import ch.so.agi.simi.entity.data.PostgresTable;
import com.haulmont.cuba.core.global.DataManager;

import java.util.Optional;
import java.util.UUID;

public class TableNameQuery {

    public static String[] execute(UUID dataSetId, DataManager dbClient){

        Optional<PostgresTable> tableInfo = dbClient.load(PostgresTable.class)
                .id(dataSetId)
                .view("dependencyService-fullTableName")
                .optional();

        if (!tableInfo.isPresent())
            throw new TableNotFoundException("No table found with id " + dataSetId);

        return new String[]{
                tableInfo.get().getDbSchema().getSchemaName(),
                tableInfo.get().getTableName()
        };
    }
}