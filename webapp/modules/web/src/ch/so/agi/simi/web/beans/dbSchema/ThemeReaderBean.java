package ch.so.agi.simi.web.beans.dbSchema;

import ch.so.agi.simi.entity.data.DbSchema;
import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.data.TableField;
import ch.so.agi.simi.web.beans.dbSchema.reader_dto.*;
import ch.so.agi.simi.web.beans.dbSchema.reader_dto.update_dto.FieldSyncState;
import ch.so.agi.simi.web.beans.dbSchema.reader_dto.update_dto.SyncedField;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(ThemeReaderBean.NAME)
public class ThemeReaderBean {
    public static final String NAME = "simi_ThemeReaderBean";

    public static Logger log = LoggerFactory.getLogger(ThemeReaderBean.class);

    public void actualizeWithDbCat(SchemaReader reader, DbSchema inOutTheme) {
        String[] tableNames = readTableNames(reader, inOutTheme);

        for (String name : tableNames) {
            PostgresTable inOutTable = inOutTheme.findTableByName(name);
            if(inOutTable == null){
                inOutTable = new PostgresTable();
                inOutTable.setTableName(name);

                if(inOutTheme.getPostgresTables() == null)
                    inOutTheme.setPostgresTables(new ArrayList<>());

                inOutTheme.getPostgresTables().add(inOutTable);
                inOutTable.setDbSchema(inOutTheme);
            }

            actualizeWithDbCat(reader, inOutTable);
        }
    }

    /**
     * Typischerweise genutzt für DB-Views (inOutTable = View). Fügt der View und deren Attribute die
     * Beschreibung der im Attribut PostgresTable.docTableName konfigurierten "Muttertabelle" bei.
     * @param reader Client-Proxy des SchemaReader-Service.
     * @param inOutTable PostgresTable-Entity, deren Beschreibung aus dem Katalog aktualisiert werden.
     * @param inOutFields Liste der synced-fields. deren Beschreibungen aus dem Katalog aktualisiert werden.
     */
    public void appendModelDoc(SchemaReader reader, PostgresTable inOutTable, List<SyncedField> inOutFields){
        if(inOutTable.getDocTableName() == null)
            return;

        TableAndFieldInfo tfi = reader.queryTableMeta(
                inOutTable.getDbSchema().getPostgresDB().getIdentifier(),
                inOutTable.getDbSchema().getSchemaName(),
                inOutTable.getDocTableName()
        );

        inOutTable.setDescriptionModel(tfi.getTableInfo().getDescription());

        Map<String, String> desc = tfi.getFields().stream()
                .filter(fieldInfo -> fieldInfo.getDescription() != null)
                .collect(Collectors.toMap(FieldInfo::getName, FieldInfo::getDescription));

        for(SyncedField sf : inOutFields){
            if(!desc.containsKey(sf.getTableField().getName()))
                continue;

            sf.getTableField().setDescriptionModel(desc.get(sf.getTableField().getName()));
        }
    }

    /**
     * Aktualisiert die Eigenschaften und zugeordneten Attribute der Entity inOutTable aufgrund der Katalogdaten der Quell-Datenbank.
     * Siehe Erläuterungen in /dok_extensions
     * @param reader Client-Proxy des SchemaReader-Service.
     * @param inOutTable PostgresTable-Entity, deren Eigenschaften und Attribute aus dem Katalog aktualisiert werden.
     * @return Liste der SyncedFields = full outer join der vorgängig in SIMI vorhandenen Felder und der aktuell in der Quelle vorhandenen Felder.
     */
    public List<SyncedField> actualizeWithDbCat(SchemaReader reader, PostgresTable inOutTable) {

        TableAndFieldInfo tfi = reader.queryTableMeta(
                inOutTable.getDbSchema().getPostgresDB().getIdentifier(),
                inOutTable.getDbSchema().getSchemaName(),
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
        inOutTable.setTableIsView(ti.isDbView());
        inOutTable.setCatSyncStamp(LocalDateTime.now());

        ArrayList<String> geoFieldTypes = new ArrayList<>();
        ArrayList<String> geoFieldNames = new ArrayList<>();
        HashSet<Integer> geoEpsgCodes = new HashSet<>();

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

            if(geoEpsgCodes.size() == 1)
                inOutTable.setGeoEpsgCode(geoEpsgCodes.stream().findFirst().get());
            else
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
            else if (fi.getGeoFieldType() == null || fi.getGeoFieldType().length() == 0) { // Feld ist nicht die Geometrie
                TableField nf = new TableField();
                nf.setCatSynced(true);
                nf.setDescriptionModel(fi.getDescription());
                nf.setMandatory(fi.getMandatory());
                nf.setName(fi.getName());
                nf.setIliEnum(fi.isIliEnum());
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

        alias = deferGermanUmlaut(alias);

        String firstChar = fieldName.substring(0,1);
        alias = alias.replaceFirst(
                firstChar,
                firstChar.toUpperCase()
        );

        return alias;
    }

    /**
     * Replaces ae, oe, ue with the corresponding german "Umlaut".
     * @param fieldName Input string
     * @return Deferred output string with replaced "Umlauts".
     */
    private static String deferGermanUmlaut(String fieldName){
        String res = fieldName;

        res = res.replaceAll("([u][e])", "ü");
        res = res.replaceAll("([a][e])", "ä");
        res = res.replaceAll("([o][e])", "ö");

        return res;
    }

    private static String[] readTableNames(SchemaReader reader, DbSchema theme) {

        ArrayList<String> relevantTables = new ArrayList<>();

        TableListing tl = reader.querySchemaMeta(
                theme.getPostgresDB().getIdentifier(),
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
