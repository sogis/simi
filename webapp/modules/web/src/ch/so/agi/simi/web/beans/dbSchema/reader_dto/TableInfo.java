package ch.so.agi.simi.web.beans.dbSchema.reader_dto;

public class TableInfo extends TableShortInfo {

    private String description;
    private String pkField;

    private boolean dbView;

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

    public boolean isDbView() {
        return dbView;
    }

    public void setDbView(boolean dbView) {
        this.dbView = dbView;
    }
}