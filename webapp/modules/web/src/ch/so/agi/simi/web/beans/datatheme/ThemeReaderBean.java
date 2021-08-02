package ch.so.agi.simi.web.beans.datatheme;

import ch.so.agi.simi.entity.data.DataTheme;
import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.data.TableField;
import ch.so.agi.simi.web.beans.datatheme.reader_dto.*;
import ch.so.agi.simi.web.beans.datatheme.reader_dto.update_dto.FieldSyncState;
import ch.so.agi.simi.web.beans.datatheme.reader_dto.update_dto.SyncedField;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component(ThemeReaderBean.NAME)
public class ThemeReaderBean {
    public static final String NAME = "simi_ThemeReaderBean";

    public static Logger log = LoggerFactory.getLogger(ThemeReaderBean.class);

    public void actualizeWithDbCat(SchemaReader reader, DataTheme inOutTheme) {
        String[] tableNames = readTableNames(reader, inOutTheme);

        for (String name : tableNames) {
            PostgresTable inOutTable = inOutTheme.findTableByName(name);
            if(inOutTable == null){
                inOutTable = new PostgresTable();
                inOutTable.setTableName(name);

                if(inOutTheme.getPostgresTables() == null)
                    inOutTheme.setPostgresTables(new ArrayList<>());

                inOutTheme.getPostgresTables().add(inOutTable);
                inOutTable.setDataTheme(inOutTheme);
            }

            actualizeWithDbCat(reader, inOutTable);
        }
    }

    public List<SyncedField> actualizeWithDbCat(SchemaReader reader, PostgresTable inOutTable) {

        TableAndFieldInfo tfi = reader.queryTableMeta(
                inOutTable.getDataTheme().getPostgresDB().getDbName(),
                inOutTable.getDataTheme().getSchemaName(),
                inOutTable.getTableName()
        );

        updateTableInfo(inOutTable, tfi);
        List<SyncedField> syncedFields = updateFields(inOutTable, tfi);

        return syncedFields;
    }

    private static void updateTableInfo(PostgresTable inOutTable, TableAndFieldInfo tfi){

        TableInfo ti = tfi.getTableInfo();

        inOutTable.setTableName(ti.getTvName());
        inOutTable.setDescriptionModel(ti.getDescription());
        inOutTable.setIdFieldName(ti.getPkField());
        inOutTable.setCatSyncStamp(LocalDateTime.now());

        ArrayList<String> geoFieldTypes = new ArrayList<>();
        ArrayList<String> geoFieldNames = new ArrayList<>();
        ArrayList<Integer> geoEpsgCodes = new ArrayList<>();

        for (FieldInfo fi : tfi.getFields()){
            String geoFieldType = fi.getGeoFieldType();

            if(geoFieldType != null && geoFieldType.length() > 0){
                geoFieldTypes.add(geoFieldType);
                geoFieldNames.add(fi.getName());
                geoEpsgCodes.add(fi.getGeoFieldSrId());

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

        if(geoFieldTypes.size() > 0){
            inOutTable.setGeoType(String.join(", ", geoFieldTypes));
            inOutTable.setGeoFieldName(String.join(", ", geoFieldNames));
            inOutTable.setGeoEpsgCode(geoEpsgCodes.get(0));
        }

        if(geoFieldTypes.size() > 1){
            log.warn("Table or View {} has multiple geometry fields. Concatenated options into the fields", inOutTable.getTableName());
            inOutTable.setGeoEpsgCode(null);
        }
    }

    private static List<SyncedField> updateFields(PostgresTable inOutTable, TableAndFieldInfo tfi){

        ArrayList<SyncedField> synced = new ArrayList<>(); // preexisting FieldInfos

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
                synced.add(new SyncedField(tf, FieldSyncState.POSSIBLY_ACTUALIZED));
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
                synced.add(new SyncedField(nf, FieldSyncState.NEW));
            }
        }

        // all still having catsynced = false are stale
        for(TableField tf : inOutTable.getTableFields()) {
            if(!tf.getCatSynced()){
                synced.add(new SyncedField(tf, FieldSyncState.STALE));
            }
        }

        return synced;
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

        String firstChar = fieldName.substring(0,1);
        alias = alias.replaceFirst(
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

        if(tl.getTableViewList() != null) {
            for (TableShortInfo ti : tl.getTableViewList()) {
                if (ti.getTvName().toLowerCase().startsWith("t_ili2db"))
                    continue;

                relevantTables.add(ti.getTvName());
            }
        }

        return relevantTables.toArray(new String[0]);
    }
}
