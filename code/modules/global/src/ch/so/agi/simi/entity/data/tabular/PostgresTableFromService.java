package ch.so.agi.simi.entity.data.tabular;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

@MetaClass(name = "simiData_PostgresTableFromService")
@NamePattern("%s.%s|schema,table")
public class PostgresTableFromService extends BaseUuidEntity {
    private static final long serialVersionUID = -6377080672960855392L;

    @MetaProperty
    private String schema;

    @MetaProperty
    private String table;

    @MetaProperty
    private String idFieldName;

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}