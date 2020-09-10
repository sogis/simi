package ch.so.agi.simi.entity.data.tabular.schemareader;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

@MetaClass(name = "simiData_FieldInfo")
public class FieldInfo extends BaseUuidEntity {
    private static final long serialVersionUID = -3395094517488093207L;

    @MetaProperty
    private String name;

    @MetaProperty
    private Boolean mandatory;

    @MetaProperty
    private String type;

    @MetaProperty
    private Integer length;

    @MetaProperty
    private String description;

    @MetaProperty
    private String geoFieldType;

    @MetaProperty
    private String geoFieldSrOrg;

    @MetaProperty
    private Integer geoFieldSrId;

    public Integer getGeoFieldSrId() {
        return geoFieldSrId;
    }

    public void setGeoFieldSrId(Integer geoFieldSrId) {
        this.geoFieldSrId = geoFieldSrId;
    }

    public String getGeoFieldSrOrg() {
        return geoFieldSrOrg;
    }

    public void setGeoFieldSrOrg(String geoFieldSrOrg) {
        this.geoFieldSrOrg = geoFieldSrOrg;
    }

    public String getGeoFieldType() {
        return geoFieldType;
    }

    public void setGeoFieldType(String geoFieldType) {
        this.geoFieldType = geoFieldType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}