package ch.so.agi.simi.web.beans.datatheme;

import ch.so.agi.simi.entity.data.DataTheme;
import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.data.TableField;
import ch.so.agi.simi.entity.data.schemareader.*;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component(ThemeReaderBean.NAME)
public class ThemeReaderBean {
    public static final String NAME = "simi_ThemeReaderBean";

    public static Logger log = LoggerFactory.getLogger(ThemeReaderBean.class);

    public void actualizeWithDbCat(SchemaReader reader, DataTheme inOutTheme) {
        String[] tableNames = readTableNames(reader, inOutTheme);

        for (String name : tableNames) {
            actualizeWithDbCat(reader, inOutTheme, name);
        }
    }

    public void actualizeWithDbCat(SchemaReader reader, DataTheme inOutTheme, String tableName) {

        TableAndFieldInfo tfi = reader.queryTableMeta(
                inOutTheme.getPostgresDB().getDbName(),
                inOutTheme.getSchemaName(),
                tableName
        );

        PostgresTable inOutTable = inOutTheme.findTableByName(tableName);
        if(inOutTable == null){
            inOutTable = new PostgresTable();

            if(inOutTheme.getPostgresTables() == null)
                inOutTheme.setPostgresTables(new ArrayList<>());

            inOutTheme.getPostgresTables().add(inOutTable);
            inOutTable.setDataTheme(inOutTheme);
        }

        updateTableInfo(inOutTable, tfi);
        updateFields(inOutTable, tfi);
    }

    private static void updateTableInfo(PostgresTable inOutTable, TableAndFieldInfo tfi){
        TableInfo ti = tfi.getTableInfo();

        inOutTable.setTableName(ti.getTvName());
        inOutTable.setDescriptionModel(ti.getDescription());
        inOutTable.setIdFieldName(ti.getPkField());

        if(inOutTable.getIdFieldName() == null || inOutTable.getIdFieldName().length() == 0) //$td remove after working ui
            inOutTable.setIdFieldName("dummy");
        
        inOutTable.setCatSyncStamp(LocalDateTime.now());

        int count = 0;
        for (FieldInfo fi : tfi.getFields()){
            String geoFieldType = fi.getGeoFieldType();

            if(geoFieldType != null && geoFieldType.length() > 0){
                count++;
                inOutTable.setGeoType(geoFieldType);
                inOutTable.setGeoFieldName(fi.getName());
                inOutTable.setGeoEpsgCode(fi.getGeoFieldSrId());

                if(!"epsg".equalsIgnoreCase(fi.getGeoFieldSrOrg())) {
                    throw new RuntimeException(
                            MessageFormat.format(
                                    "Can only handle epsg crs codes, but got code from organization '{0}'",
                                    fi.getGeoFieldSrOrg()
                            )
                    );
                }
            }
        }

        if(count > 1){
            log.warn("Table or View {} has multiple geometry fields. Using {}", inOutTable.getTableName(), inOutTable.getGeoFieldName());
        }
    }

    private static void updateFields(PostgresTable inOutTable, TableAndFieldInfo tfi){

        if(inOutTable.getTableFields() == null){
            inOutTable.setTableFields(new ArrayList<>());
        }

        for(TableField tf : inOutTable.getTableFields()) {
            tf.setCatSynced(false);
        }

        for (FieldInfo fi : tfi.getFields()) {
            TableField tf = inOutTable.findField(fi.getName());

            if(tf != null && areEqualFields(tf, fi)) {
                tf.setCatSynced(true);
                tf.setDescriptionModel(fi.getDescription());
            }
            else if (fi.getGeoFieldType() == null || fi.getGeoFieldType().length() == 0) {
                TableField nf = new TableField();
                nf.setCatSynced(true);
                nf.setDescriptionModel(fi.getDescription());
                nf.setMandatory(fi.getMandatory());
                nf.setName(fi.getName());
                nf.setTypeName(fi.getType());
                nf.setStrLength(fi.getLength());
                nf.setAlias(buildAliasFromName(fi.getName()));

                nf.setPostgresTable(inOutTable);
                inOutTable.getTableFields().add(nf);
            }
        }
    }

    private static boolean areEqualFields(TableField tf, FieldInfo  fi){ //only compares field props. Assumes that tables are equal
        if(tf == null || fi == null)
            return false;

        boolean nameEqual = tf.getName().toLowerCase().equals(fi.getName().toLowerCase());
        boolean typeEqual = tf.getTypeName().toLowerCase().equals(fi.getType().toLowerCase());

        return nameEqual && typeEqual;
    }

    private static String buildAliasFromName(String fieldName){
        String alias = fieldName.replace("_", " ");

        String firstChar = String.valueOf(fieldName.charAt(0));
        alias.replaceFirst(
                firstChar,
                firstChar.toUpperCase()
        );

        return alias;
    }

    private static String[] readTableNames(SchemaReader reader, DataTheme theme) {

        ArrayList<String> relevantTables = new ArrayList<>();

        TableListing tl = reader.querySchemaMeta(
                theme.getPostgresDB().getDbName(),
                theme.getSchemaName()
        );

        for(TableShortInfo ti : tl.getTableViewList()){
            if(ti.getTvName().toLowerCase().startsWith("t_ili2db"))
                continue;

            relevantTables.add(ti.getTvName());
        }

        return relevantTables.toArray(new String[0]);
    }
}
