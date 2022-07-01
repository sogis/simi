package ch.so.agi.simi.web.beans.dbSchema;

import ch.so.agi.simi.web.beans.dbSchema.reader_dto.TableAndFieldInfo;
import ch.so.agi.simi.web.beans.dbSchema.reader_dto.TableListing;
import ch.so.agi.simi.web.config.SchemareaderConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Component(SchemaReaderClientBean.NAME)
public class SchemaReaderClientBean implements SchemaReader {
    public static final String NAME = "simi_SchemaReaderClientBean";

    private RestTemplate restTemplate = new RestTemplate();

    @Inject
    private SchemareaderConfig schemareaderConfig;

    @Override
    public TableAndFieldInfo queryTableMeta(String dbName, String schemaName, String tableName){

        String request_uri = schemareaderConfig.getBaseUrl() + "/{db}/{schema}/{table}";

        Map<String, String> urlParameters = new HashMap<>();
        urlParameters.put("db", dbName);
        urlParameters.put("schema", schemaName);
        urlParameters.put("table", tableName);

        TableAndFieldInfo tfi = restTemplate.getForObject(request_uri, TableAndFieldInfo.class, urlParameters);

        return tfi;
    }

    @Override
    public TableListing querySchemaMeta(String dbName, String schemaName){

        String request_uri = schemareaderConfig.getBaseUrl() + "/{db}?schema={schema}";

        Map<String, String> urlParameters = new HashMap<>();
        urlParameters.put("db", dbName);
        urlParameters.put("schema", schemaName);

        return restTemplate.getForObject(request_uri, TableListing.class, urlParameters);
    }
}