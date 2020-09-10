package ch.so.agi.simi.entity.data.tabular.schemareader;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;

@MetaClass(name = "simiData_TableInfo")
public class TableInfo extends TableShortInfo {
    private static final long serialVersionUID = -791139972778760579L;

    @MetaProperty
    private String description;

    @MetaProperty
    private String pkField;

    public String getPkField() {
        return pkField;
    }

    public void setPkField(String pkField) {
        this.pkField = pkField;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}