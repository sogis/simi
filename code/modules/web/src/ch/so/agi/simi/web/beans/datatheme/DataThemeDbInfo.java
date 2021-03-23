package ch.so.agi.simi.web.beans.datatheme;

import ch.so.agi.simi.web.beans.datatheme.reader_dto.TableAndFieldInfo;

import java.util.HashMap;

public class DataThemeDbInfo {

    private HashMap<String, TableAndFieldInfo> tables;

    DataThemeDbInfo(HashMap<String, TableAndFieldInfo> tables){
        this.tables = tables;
    }

    public TableAndFieldInfo getOrNull(String tableName){
        return tables.getOrDefault(tableName, null);
    }
}
