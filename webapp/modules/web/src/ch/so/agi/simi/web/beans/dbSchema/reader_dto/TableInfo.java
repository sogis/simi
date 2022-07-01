package ch.so.agi.simi.web.beans.dbSchema.reader_dto;

public class TableInfo extends TableShortInfo {

    private String description;
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