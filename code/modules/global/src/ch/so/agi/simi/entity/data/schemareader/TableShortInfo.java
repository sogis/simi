package ch.so.agi.simi.entity.data.schemareader;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

@MetaClass(name = "simiData_TableShortInfo")
public class TableShortInfo extends BaseUuidEntity {
    private static final long serialVersionUID = 3386796722209366587L;

    @MetaProperty
    private String schemaName;

    @MetaProperty
    private String tvName;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }
}