package ch.so.agi.simi.web;

import ch.so.agi.simi.entity.data.tabular.PostgresDB;
import ch.so.agi.simi.entity.data.tabular.schemareader.TableAndFieldInfo;
import ch.so.agi.simi.entity.data.tabular.schemareader.TableListing;
import ch.so.agi.simi.entity.data.tabular.schemareader.TableShortInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component(SchemaReaderBean.NAME)
public class SchemaReaderBean {
    public static final String NAME = "simi_SchemaReaderBean";

    public TableListing getTableSearch(PostgresDB postgresDB, String schema, String table) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(TABLE_SEARCH, TableListing.class);
    }

    public TableAndFieldInfo getTableInfo(PostgresDB postgresDB, TableShortInfo tableShortInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(TABLE_INFO, TableAndFieldInfo.class);
    }

    private static String TABLE_SEARCH = "{\n" +
            "\t\"tableViewList\": [{\n" +
            "\t\t\"schemaName\": \"tiger\",\n" +
            "\t\t\"tvName\": \"county\"\n" +
            "\t}, {\n" +
            "\t\t\"schemaName\": \"tiger\",\n" +
            "\t\t\"tvName\": \"county_lookup\"\n" +
            "\t}, {\n" +
            "\t\t\"schemaName\": \"tiger\",\n" +
            "\t\t\"tvName\": \"countysub_lookup\"\n" +
            "\t}, {\n" +
            "\t\t\"schemaName\": \"tiger\",\n" +
            "\t\t\"tvName\": \"cousub\"\n" +
            "\t}],\n" +
            "\t\"truncatedTo\": null\n" +
            "}";

    private static String TABLE_INFO = "{\n" +
            "\t\"tableInfo\": {\n" +
            "\t\t\"schemaName\": \"tiger\",\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"pkField\": \"cntyidfp\",\n" +
            "\t\t\"tvName\": \"county\"\n" +
            "\t},\n" +
            "\t\"fields\": [{\n" +
            "\t\t\"name\": \"gid\",\n" +
            "\t\t\"mandatory\": true,\n" +
            "\t\t\"type\": \"int4\",\n" +
            "\t\t\"length\": null,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"statefp\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 2,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"countyfp\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 3,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"countyns\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 8,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"cntyidfp\",\n" +
            "\t\t\"mandatory\": true,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 5,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"name\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 100,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"namelsad\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 100,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"lsad\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 2,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"classfp\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 2,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"mtfcc\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 5,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"csafp\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 3,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"cbsafp\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 5,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"metdivfp\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 5,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"funcstat\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 1,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"aland\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"int8\",\n" +
            "\t\t\"length\": null,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"awater\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"float8\",\n" +
            "\t\t\"length\": null,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"intptlat\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 11,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"intptlon\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"varchar\",\n" +
            "\t\t\"length\": 12,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": null,\n" +
            "\t\t\"geoFieldSrOrg\": null,\n" +
            "\t\t\"geoFieldSrId\": null\n" +
            "\t}, {\n" +
            "\t\t\"name\": \"the_geom\",\n" +
            "\t\t\"mandatory\": false,\n" +
            "\t\t\"type\": \"geometry\",\n" +
            "\t\t\"length\": null,\n" +
            "\t\t\"description\": null,\n" +
            "\t\t\"geoFieldType\": \"MULTIPOLYGON\",\n" +
            "\t\t\"geoFieldSrOrg\": \"EPSG\",\n" +
            "\t\t\"geoFieldSrId\": 4269\n" +
            "\t}]\n" +
            "}";
}