package ch.so.agi.simi.web.beans.dbSchema;

import ch.so.agi.simi.web.beans.dbSchema.reader_dto.TableAndFieldInfo;

import java.util.HashMap;

public class DbSchemaDbInfo {

    private HashMap<String, TableAndFieldInfo> tables;

    DbSchemaDbInfo(HashMap<String, TableAndFieldInfo> tables){
        this.tables = tables;
    }

    public TableAndFieldInfo getOrNull(String tableName){
        return tables.getOrDefault(tableName, null);
    }
}
