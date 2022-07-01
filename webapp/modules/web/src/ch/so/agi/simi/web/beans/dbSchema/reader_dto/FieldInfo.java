package ch.so.agi.simi.web.beans.dbSchema.reader_dto;

public class FieldInfo {

    private String name;
    private Boolean mandatory;
    private String type;
    private Integer length;
    private String description;
    private String geoFieldType;
    private String geoFieldSrOrg;
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