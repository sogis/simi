package ch.so.agi.simi.web.beans.datatheme;

import ch.so.agi.simi.web.beans.datatheme.reader_dto.TableAndFieldInfo;
import ch.so.agi.simi.web.beans.datatheme.reader_dto.TableListing;

public interface SchemaReader {
    TableAndFieldInfo queryTableMeta(String dbName, String schemaName, String tableName);

    TableListing querySchemaMeta(String dbName, String schemaName);
}
