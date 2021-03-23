package ch.so.agi.simi.web.beans.datatheme;

import ch.so.agi.simi.web.beans.datatheme.dto.TableAndFieldInfo;
import ch.so.agi.simi.web.beans.datatheme.dto.TableListing;

public interface SchemaReader {
    //$td umwandeln auf Info von genau einer Tabelle
    //DataThemeDbInfo queryDbCatalogue(String dbName, String schemaName, String[] tableNames);

    TableAndFieldInfo queryTableMeta(String dbName, String schemaName, String tableName);

    TableListing querySchemaMeta(String dbName, String schemaName);
}
