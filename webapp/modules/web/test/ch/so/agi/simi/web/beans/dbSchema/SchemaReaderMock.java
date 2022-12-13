package ch.so.agi.simi.web.beans.dbSchema;

import ch.so.agi.simi.web.beans.dbSchema.reader_dto.*;

import java.util.ArrayList;
import java.util.List;

public class SchemaReaderMock implements SchemaReader {

    @Override
    public TableListing querySchemaMeta(String dbName, String schemaName){
        TableListing res = null;

        if(ThemeReaderBeanTest.TABLE_DELETED__SCHEMA.equals(schemaName)){
            res = listingMock(schemaName, "current");
        }
        else if(ThemeReaderBeanTest.TABLE_UPDATED__SCHEMA.equals(schemaName)){
            res = listingMock(schemaName, ThemeReaderBeanTest.TABLE_UPDATED__TABLE);
        }
        else if(ThemeReaderBeanTest.TABLE_NEW__SCHEMA.equals(schemaName)){
            res = listingMock(schemaName, ThemeReaderBeanTest.TABLE_NEW__TABLE);
        }
        else if(ThemeReaderBeanTest.FIELD_DELETED__SCHEMA.equals(schemaName)){
            res = listingMock(schemaName, ThemeReaderBeanTest.FIELD_DELETED__TABLE);
        }
        else if(ThemeReaderBeanTest.FIELD_UPDATED__SCHEMA.equals(schemaName)){
            res = listingMock(schemaName, ThemeReaderBeanTest.FIELD_UPDATED__TABLE);
        }
        else if(ThemeReaderBeanTest.FIELD_NEW__SCHEMA.equals(schemaName)){
            res = listingMock(schemaName, ThemeReaderBeanTest.FIELD_NEW__TABLE);
        }
        else if(ThemeReaderBeanTest.SCHEMA_READALL__SCHEMA.equals(schemaName)){
            res = listingMock(schemaName, ThemeReaderBeanTest.SCHEMA_READALL__TABLE);
        }
        else if(ThemeReaderBeanTest.SCHEMA_EXISTING_KEPT__SCHEMA.equals(schemaName)){
            res = listingMock(schemaName, ThemeReaderBeanTest.SCHEMA_EXISTING_KEPT__NEW_TABLE);
        }

        return res;
    }

    @Override
    public TableAndFieldInfo queryTableMeta(String dbName, String schemaName, String tableName){

        TableAndFieldInfo res = null;

        if(ThemeReaderBeanTest.TABLE_DELETED__SCHEMA.equals(schemaName)){
            res = tblAndFieldMock(schemaName, "current", "fuu_");
        }
        else if(ThemeReaderBeanTest.TABLE_UPDATED__SCHEMA.equals(schemaName)){
            res = tblAndFieldMock(schemaName, ThemeReaderBeanTest.TABLE_UPDATED__TABLE, "fuu_");
        }
        else if(ThemeReaderBeanTest.TABLE_NEW__SCHEMA.equals(schemaName)){
            res = tblAndFieldMock(schemaName, ThemeReaderBeanTest.TABLE_NEW__TABLE, "fuu_");
        }
        else if(ThemeReaderBeanTest.FIELD_DELETED__SCHEMA.equals(schemaName)){
            res = tblAndFieldMock(schemaName, ThemeReaderBeanTest.FIELD_DELETED__TABLE, "fuu_");
        }
        else if(ThemeReaderBeanTest.FIELD_UPDATED__SCHEMA.equals(schemaName)){
            res = tblAndFieldMock(schemaName, ThemeReaderBeanTest.FIELD_UPDATED__TABLE, ThemeReaderBeanTest.FIELD_UPDATED__FIELD);
        }
        else if(ThemeReaderBeanTest.FIELD_NEW__SCHEMA.equals(schemaName)){
            res = tblAndFieldMock(schemaName, ThemeReaderBeanTest.FIELD_NEW__TABLE, ThemeReaderBeanTest.FIELD_NEW__FIELD);
        }
        else if(ThemeReaderBeanTest.SCHEMA_READALL__SCHEMA.equals(schemaName)){
            res = tblAndFieldMock(schemaName, ThemeReaderBeanTest.SCHEMA_READALL__TABLE, null);
            
            addAllTableProperties(res.getTableInfo());
            addAllFields(res.getFields());
        }
        else if(ThemeReaderBeanTest.SCHEMA_EXISTING_KEPT__SCHEMA.equals(schemaName)){
            res = tblAndFieldMock(schemaName, ThemeReaderBeanTest.SCHEMA_EXISTING_KEPT__NEW_TABLE, "fuu_");
        }
        else if(ThemeReaderBeanTest.VIEW_DOC_FROM_TABLE__SCHEMA.equals(schemaName)){
            res = tblAndFieldMock(schemaName, ThemeReaderBeanTest.VIEW_DOC_FROM_TABLE__TABLE,null);

            res.getTableInfo().setDescription(ThemeReaderBeanTest.VIEW_DOC_FROM_TABLE__TABLE_DESC);

            FieldInfo fi = fieldInfoMock(ThemeReaderBeanTest.VIEW_DOC_FROM_TABLE__FIELD);
            fi.setDescription(ThemeReaderBeanTest.VIEW_DOC_FROM_TABLE__FIELD_DESC);

            FieldInfo fieldNoDesc = fieldInfoMock(ThemeReaderBeanTest.VIEW_DOC_FROM_TABLE__FIELD + "_");
            fieldNoDesc.setDescription(null);

            res.getFields().add(fi);
            res.getFields().add(fieldNoDesc);
        }

        return res;
    }

    private static void addAllFields(List<FieldInfo> fields){
        
        FieldInfo g1 = new FieldInfo();
        g1.setName(ThemeReaderBeanTest.SCHEMA_READALL_FIELD_GEO1);
        g1.setType(ThemeReaderBeanTest.SCHEMA_READALL__STRING);
        g1.setGeoFieldType(ThemeReaderBeanTest.SCHEMA_READALL__STRING);
        g1.setGeoFieldSrOrg("epsg");
        g1.setGeoFieldSrId(ThemeReaderBeanTest.SCHEMA_READALL__INTEGER);
        fields.add(g1);

        FieldInfo g2 = new FieldInfo();
        g2.setName(ThemeReaderBeanTest.SCHEMA_READALL_FIELD_GEO2);
        g2.setType(ThemeReaderBeanTest.SCHEMA_READALL__STRING);
        g2.setGeoFieldType(ThemeReaderBeanTest.SCHEMA_READALL__STRING);
        g2.setGeoFieldSrOrg("epsg");
        g2.setGeoFieldSrId(ThemeReaderBeanTest.SCHEMA_READALL__INTEGER);
        fields.add(g2);

        FieldInfo charBounded = new FieldInfo();
        charBounded.setName(ThemeReaderBeanTest.SCHEMA_READALL_FIELD_CHAR_BOUNDED);
        charBounded.setType(ThemeReaderBeanTest.SCHEMA_READALL__STRING);
        charBounded.setLength(ThemeReaderBeanTest.SCHEMA_READALL__INTEGER);
        charBounded.setDescription(ThemeReaderBeanTest.SCHEMA_READALL__STRING);
        charBounded.setMandatory(ThemeReaderBeanTest.SCHEMA_READALL__BOOLEAN);
        fields.add(charBounded);

        FieldInfo charMax = new FieldInfo();
        charMax.setName(ThemeReaderBeanTest.SCHEMA_READALL_FIELD_CHAR_MAX);
        charMax.setType(ThemeReaderBeanTest.SCHEMA_READALL__STRING);
        charMax.setDescription(ThemeReaderBeanTest.SCHEMA_READALL__STRING);
        charMax.setMandatory(ThemeReaderBeanTest.SCHEMA_READALL__BOOLEAN);
        fields.add(charMax);
    }
    
    private static void addAllTableProperties(TableInfo ti){
        ti.setDescription(ThemeReaderBeanTest.SCHEMA_READALL__STRING);
        ti.setPkField(ThemeReaderBeanTest.SCHEMA_READALL__STRING);
    }

    private static TableListing listingMock(String schemaName, String tableName){
        TableListing listing = new TableListing();

        ArrayList<TableShortInfo> list = new ArrayList<>();
        listing.setTableViewList(list);

        TableShortInfo first = tblShortMock(schemaName, "first_");
        TableShortInfo mock = tblShortMock(schemaName, tableName);
        TableShortInfo last = tblShortMock(schemaName, "last_");

        list.add(first);
        list.add(mock);
        list.add(last);

        return listing;
    }

    private static TableAndFieldInfo tblAndFieldMock(String schemaName, String tableName, String fieldName){
        TableAndFieldInfo tfi = new TableAndFieldInfo();

        tfi.setTableInfo(tblLongMock(schemaName, tableName));

        ArrayList<FieldInfo> fieldInfos = new ArrayList<>();
        tfi.setFields(fieldInfos);

        if(fieldName != null && fieldName.length() > 0){
            fieldInfos.add(fieldInfoMock("first_"));
            fieldInfos.add(fieldInfoMock(fieldName));
            fieldInfos.add(fieldInfoMock("last_"));
        }

        return tfi;
    }

    private static TableInfo tblLongMock(String schemaName, String tableName){
        TableInfo ti = new TableInfo();
        ti.setSchemaName(schemaName);
        ti.setTvName(tableName);

        return ti;
    }

    private static TableShortInfo tblShortMock(String schemaName, String tableName){
        TableShortInfo si = new TableShortInfo();
        si.setSchemaName(schemaName);
        si.setTvName(tableName);

        return si;
    }

    private static FieldInfo fieldInfoMock(String name){
        FieldInfo fi = new FieldInfo();
        fi.setName(name);
        fi.setType(ThemeReaderBeanTest.FIELD_DEFAULT_TYPE);

        return fi;
    }
}
