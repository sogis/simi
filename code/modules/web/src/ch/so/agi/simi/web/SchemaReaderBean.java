package ch.so.agi.simi.web;

import ch.so.agi.simi.web.config.SchemareaderConfig;
import ch.so.agi.simi.entity.data.tabular.PostgresDB;
import ch.so.agi.simi.entity.data.tabular.schemareader.TableAndFieldInfo;
import ch.so.agi.simi.entity.data.tabular.schemareader.TableListing;
import ch.so.agi.simi.entity.data.tabular.schemareader.TableShortInfo;
import com.haulmont.cuba.core.global.TimeSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Component(SchemaReaderBean.NAME)
public class SchemaReaderBean {
    public static final String NAME = "simi_SchemaReaderBean";

    private RestTemplate restTemplate = new RestTemplate();

    @Inject
    private TimeSource timeSource;
    @Inject
    private SchemareaderConfig schemareaderConfig;

    /**
     * Request Table list from Schemareader Rest Service
     *
     * @param postgresDB The PostgresDB to search
     * @param schema The Schema search string
     * @param table The Table search string
     * @return Search result encapsulated in {@link TableListing}
     * @throws RestClientException request failed
     * @throws IllegalArgumentException required search parameters are null or empty
     */
    public TableListing getTableSearch(PostgresDB postgresDB, String schema, String table) throws RestClientException, IllegalArgumentException {
        if (postgresDB == null) {
            throw new IllegalArgumentException("Das Feld Datenbank ist leer.");
        }
        if (StringUtils.isEmpty(schema) && StringUtils.isEmpty(table)) {
            throw new IllegalArgumentException("Die Felder Schema und Tabelle können nicht beide leer sein.");
        }

        String request_uri = schemareaderConfig.getBaseUrl() + "/{db}?schema={schema}&table={table}";

        Map<String, String> urlParameters = new HashMap<>();
        urlParameters.put("db", postgresDB.getDbName());
        urlParameters.put("schema", schema);
        urlParameters.put("table", table);

        return restTemplate.getForObject(request_uri, TableListing.class, urlParameters);
    }

    /**
     * Request details to a table from Schemareader Rest Service
     *
     * @param postgresDB The PostgresDB containing the table described in {@code tableShortInfo}
     * @param tableShortInfo The Schema and Table information
     * @return {@link TableAndFieldInfo} containing detailed information about fields inside the table.
     * @throws RestClientException request failed
     */
    public TableAndFieldInfo getTableInfo(PostgresDB postgresDB, TableShortInfo tableShortInfo) throws RestClientException {
        String request_uri = schemareaderConfig.getBaseUrl() + "/{db}/{schema}/{table}";

        Map<String, String> urlParameters = new HashMap<>();
        urlParameters.put("db", postgresDB.getDbName());
        urlParameters.put("schema", tableShortInfo.getSchemaName());
        urlParameters.put("table", tableShortInfo.getTvName());

        TableAndFieldInfo tableAndFieldInfo = restTemplate.getForObject(request_uri, TableAndFieldInfo.class, urlParameters);
        tableAndFieldInfo.setCatSyncStamp(timeSource.now().toLocalDateTime());
        tableAndFieldInfo.setPostgresDB(postgresDB);
        return tableAndFieldInfo;
    }
}