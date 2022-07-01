package ch.so.agi.simi.web.beans.dbSchema;

import ch.so.agi.simi.web.beans.dbSchema.reader_dto.TableAndFieldInfo;
import ch.so.agi.simi.web.beans.dbSchema.reader_dto.TableListing;

public interface SchemaReader {
    TableAndFieldInfo queryTableMeta(String dbName, String schemaName, String tableName);

    TableListing querySchemaMeta(String dbName, String schemaName);
}
