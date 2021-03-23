package ch.so.agi.simi.web.beans.datatheme;

import ch.so.agi.simi.entity.data.DataTheme;
import ch.so.agi.simi.entity.data.PostgresDB;
import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.data.TableField;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ThemeReaderBeanTest {

    public static final LocalDateTime TIME_NOW = LocalDateTime.now();
    public static final LocalDateTime TIME_BEFORE = LocalDateTime.now().minusDays(1);

    public static final SchemaReaderMock READER_MOCK = new SchemaReaderMock();

    public static final String FIELD_DEFAULT_TYPE = "varchar";
    public static final String FIELD_DEFAULT_NAME = "myfield";

    public static final String SCHEMA_READALL__SCHEMA = "schema_read_all";
    public static final String SCHEMA_READALL__TABLE = "mytable";
    public static final String SCHEMA_READALL__STRING = "mystring";
    public static final int SCHEMA_READALL__INTEGER = 99;
    public static final boolean SCHEMA_READALL__BOOLEAN = true;
    public static final String SCHEMA_READALL_FIELD_GEO1 = "geom1";
    public static final String SCHEMA_READALL_FIELD_GEO2 = "geom2";
    public static final String SCHEMA_READALL_FIELD_CHAR_BOUNDED = "charBounded";
    public static final String SCHEMA_READALL_FIELD_CHAR_MAX = "charMax";

    public static final String SCHEMA_EXISTING_KEPT__SCHEMA = "schema_existing_kept_schema";
    public static final String SCHEMA_EXISTING_KEPT__OLD_TABLE = "old_table";
    public static final String SCHEMA_EXISTING_KEPT__NEW_TABLE = "new_table";

    public static final String TABLE_DELETED__SCHEMA = "table_deleted_schema";
    public static final String TABLE_DELETED__TABLE = "stale";

    public static final String TABLE_UPDATED__SCHEMA = "table_updated_schema";
    public static final String TABLE_UPDATED__TABLE = "updated";

    public static final String TABLE_NEW__SCHEMA = "table_new_schema";
    public static final String TABLE_NEW__TABLE = "newtable";

    public static final String FIELD_DELETED__SCHEMA = "field_deleted_schema";
    public static final String FIELD_DELETED__TABLE = "fieldtable";
    public static final String FIELD_DELETED__FIELD = "stale";

    public static final String FIELD_UPDATED__SCHEMA = "field_updated_schema";
    public static final String FIELD_UPDATED__TABLE = "fieldtable";
    public static final String FIELD_UPDATED__FIELD = "updated";

    public static final String FIELD_NEW__SCHEMA = "field_new_schema";
    public static final String FIELD_NEW__TABLE = "fieldtable";
    public static final String FIELD_NEW__FIELD = "newfield";

    @Test
    public void schema_ReadAllTableAndFieldProperties_OK(){
        ThemeReaderBean bean = new ThemeReaderBean();

        DataTheme theme = themeCreateMock();
        PostgresTable stale = themeAddNewTable(theme);

        theme.setSchemaName(SCHEMA_READALL__SCHEMA);
        bean.actualizeWithDbCat(READER_MOCK, theme);

        //Assert that the stale table is still contained and keeps old timestamp
        PostgresTable table = theme.findTableByName(SCHEMA_READALL__TABLE);
        Assertions.assertNotNull(table);

        Assertions.assertEquals(theme, table.getDataTheme());
        Assertions.assertEquals(SCHEMA_READALL__STRING, table.getIdFieldName());
        Assertions.assertEquals(SCHEMA_READALL__STRING, table.getDescriptionModel());
        Assertions.assertEquals(SCHEMA_READALL__INTEGER, table.getGeoEpsgCode());
        Assertions.assertEquals(SCHEMA_READALL_FIELD_GEO2, table.getGeoFieldName());
        Assertions.assertEquals(SCHEMA_READALL__STRING, table.getGeoType());

        for(TableField tf : table.getTableFields()){
            Assertions.assertEquals(SCHEMA_READALL__STRING, tf.getDescriptionModel());
            Assertions.assertEquals(SCHEMA_READALL__STRING, tf.getTypeName());
            Assertions.assertEquals(SCHEMA_READALL__BOOLEAN, tf.getMandatory());
            Assertions.assertNotNull(tf.getAlias());

            String aliasFirstChar = tf.getAlias().substring(0,1);
            String nameFirstChar = tf.getName().substring(0,1);
            Assertions.assertEquals(nameFirstChar.toUpperCase(), aliasFirstChar);

            if(SCHEMA_READALL_FIELD_CHAR_BOUNDED.equals(tf.getName())){
                Assertions.assertEquals(SCHEMA_READALL__INTEGER, tf.getStrLength());
            }
        }
    }

    @Test
    public void schema_ExistingTablesAreKept_OK(){
        ThemeReaderBean bean = new ThemeReaderBean();

        DataTheme theme = themeCreateMock();
        PostgresTable stale = themeAddNewTable(theme);

        theme.setSchemaName(SCHEMA_EXISTING_KEPT__SCHEMA);
        stale.setTableName(SCHEMA_EXISTING_KEPT__OLD_TABLE);
        stale.setCatSyncStamp(TIME_BEFORE);

        bean.actualizeWithDbCat(READER_MOCK, theme);

        //Assert that old and new tables are contained
        PostgresTable oldTable = theme.findTableByName(SCHEMA_EXISTING_KEPT__OLD_TABLE);
        Assertions.assertNotNull(oldTable);

        PostgresTable newTable = theme.findTableByName(SCHEMA_EXISTING_KEPT__NEW_TABLE);
        Assertions.assertNotNull(newTable);
    }

    @Test
    public void table_DeletedKeepOldTimeStamp_OK(){
        ThemeReaderBean bean = new ThemeReaderBean();

        DataTheme theme = themeCreateMock();
        PostgresTable stale = themeAddNewTable(theme);

        theme.setSchemaName(TABLE_DELETED__SCHEMA);
        stale.setTableName(TABLE_DELETED__TABLE);
        stale.setCatSyncStamp(TIME_BEFORE);

        bean.actualizeWithDbCat(READER_MOCK, theme);

        //Assert that the stale table is still contained and keeps old timestamp
        PostgresTable updated = theme.findTableByName(TABLE_DELETED__TABLE);

        Assertions.assertNotNull(updated);
        Assertions.assertEquals(stale.getCatSyncStamp(), updated.getCatSyncStamp(), "Stale table must keep it's previous (old) timestamp");
    }

    @Test
    public void table_ExistingAreUpdatedInPlace_OK(){
        ThemeReaderBean bean = new ThemeReaderBean();

        DataTheme theme = themeCreateMock();
        PostgresTable beforeAct = themeAddNewTable(theme);

        theme.setSchemaName(TABLE_UPDATED__SCHEMA);
        beforeAct.setTableName(TABLE_UPDATED__TABLE);
        beforeAct.setCatSyncStamp(TIME_BEFORE);

        bean.actualizeWithDbCat(READER_MOCK, theme);

        PostgresTable afterAct = theme.findTableByName(TABLE_UPDATED__TABLE);

        Assertions.assertNotNull(afterAct);
        Assertions.assertTrue(TIME_BEFORE.isBefore(afterAct.getCatSyncStamp()), "Timestamp for existing table must be actualized");
    }

    @Test
    public void table_NewOnesAreAdded_OK(){
        ThemeReaderBean bean = new ThemeReaderBean();

        DataTheme theme = themeCreateMock();
        PostgresTable existing = themeAddNewTable(theme);

        theme.setSchemaName(TABLE_NEW__SCHEMA);
        existing.setTableName("fuu");
        existing.setCatSyncStamp(TIME_BEFORE);

        bean.actualizeWithDbCat(READER_MOCK, theme);

        PostgresTable newTable = theme.findTableByName(TABLE_NEW__TABLE);

        Assertions.assertNotNull(newTable);
        Assertions.assertTrue(TIME_BEFORE.isBefore(newTable.getCatSyncStamp()), "Timestamp for new Table must be the current time");
    }

    @Test
    public void field_DeletedGetStaleMark_OK(){
        ThemeReaderBean bean = new ThemeReaderBean();

        DataTheme theme = themeCreateMock();
        PostgresTable existing = themeAddNewTable(theme);

        theme.setSchemaName(FIELD_DELETED__SCHEMA);
        existing.setTableName(FIELD_DELETED__TABLE);
        existing.setCatSyncStamp(TIME_BEFORE);
        existing.getTableFields().get(0).setName(FIELD_DELETED__FIELD);

        bean.actualizeWithDbCat(READER_MOCK, theme);

        PostgresTable updatedTable = theme.findTableByName(FIELD_DELETED__TABLE);
        Assertions.assertNotNull(updatedTable);

        TableField staleField = updatedTable.findField(FIELD_DELETED__FIELD);
        Assertions.assertNotNull(staleField);
        Assertions.assertFalse(staleField.getCatSynced());
    }

    @Test
    public void field_ExistingAreUpdatedInPlace_OK(){
        ThemeReaderBean bean = new ThemeReaderBean();

        DataTheme theme = themeCreateMock();
        PostgresTable existing = themeAddNewTable(theme);

        theme.setSchemaName(FIELD_UPDATED__SCHEMA);
        existing.setTableName(FIELD_UPDATED__TABLE);
        existing.setCatSyncStamp(TIME_BEFORE);
        existing.getTableFields().get(0).setName(FIELD_UPDATED__FIELD);
        existing.getTableFields().get(0).setAlias(FIELD_UPDATED__FIELD);
        existing.getTableFields().get(0).setCatSynced(true);

        bean.actualizeWithDbCat(READER_MOCK, theme);

        PostgresTable updatedTable = theme.findTableByName(FIELD_UPDATED__TABLE);
        Assertions.assertNotNull(updatedTable);

        TableField updatedField = updatedTable.findField(FIELD_UPDATED__FIELD);
        Assertions.assertNotNull(updatedField);
        Assertions.assertTrue(updatedField.getCatSynced());
        Assertions.assertEquals(FIELD_UPDATED__FIELD, updatedField.getAlias(), "Alias must remain unchanged");
    }

    @Test
    public void field_NewOnesAreAdded_OK(){
        ThemeReaderBean bean = new ThemeReaderBean();

        DataTheme theme = themeCreateMock();
        PostgresTable existing = themeAddNewTable(theme);

        theme.setSchemaName(FIELD_NEW__SCHEMA);
        existing.setTableName(FIELD_NEW__TABLE);
        existing.setCatSyncStamp(TIME_BEFORE);

        bean.actualizeWithDbCat(READER_MOCK, theme);

        PostgresTable updatedTable = theme.findTableByName(FIELD_NEW__TABLE);
        Assertions.assertNotNull(updatedTable);

        TableField newField = updatedTable.findField(FIELD_NEW__FIELD);
        Assertions.assertNotNull(newField);
        Assertions.assertTrue(newField.getCatSynced());
    }

    private static PostgresTable themeAddNewTable(DataTheme theme){
        PostgresTable tbl = new PostgresTable();

        if(theme.getPostgresTables() == null)
            theme.setPostgresTables(new ArrayList<>());

        theme.getPostgresTables().add(tbl);
        tbl.setDataTheme(theme);

        //Add default TableField
        TableField tf = new TableField();
        tf.setName(FIELD_DEFAULT_NAME);
        tf.setTypeName(FIELD_DEFAULT_TYPE);

        ArrayList<TableField> fields = new ArrayList<>();
        fields.add(tf);

        tbl.setTableFields(fields);

        return tbl;
    }

    private static DataTheme themeCreateMock(){
        PostgresDB db = new PostgresDB();
        db.setDbName("mydb");

        DataTheme theme = new DataTheme();
        theme.setPostgresDB(db);

        return theme;
    }

}
