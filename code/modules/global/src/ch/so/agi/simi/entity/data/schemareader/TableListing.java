package ch.so.agi.simi.entity.data.schemareader;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import java.util.List;

@MetaClass(name = "simiData_TableListing")
public class TableListing extends BaseUuidEntity {
    private static final long serialVersionUID = -801278764240894912L;

    @MetaProperty
    private List<TableShortInfo> tableViewList;

    @MetaProperty
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