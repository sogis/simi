package ch.so.agi.simi.web.beans.dbSchema.reader_dto;

import java.util.List;

public class TableListing {

    private List<TableShortInfo> tableViewList;
    private Integer truncatedTo;

    public Integer getTruncatedTo() {
        return truncatedTo;
    }

    public void setTruncatedTo(Integer truncatedTo) {
        this.truncatedTo = truncatedTo;
    }

    public void setTableViewList(List<TableShortInfo> tableViewList) {
        this.tableViewList = tableViewList;
    }

    public List<TableShortInfo> getTableViewList() {
        return tableViewList;
    }

}