package ch.so.agi.simi.web.beans.datatheme.reader_dto;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

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