package ch.so.agi.simi.entity.data.datasetview;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.Sortable;
import ch.so.agi.simi.entity.data.TableField;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIDATA_VIEW_FIELD", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_SIMI_VIEW_FIELD_UNQ_TABLE_FIELD_ID_TABLE_VIEW_ID", columnNames = {"TABLE_FIELD_ID", "TABLE_VIEW_ID"})
})
@Entity(name = ViewField.NAME)
public class ViewField extends SimiEntity implements Sortable {

    public static final String NAME = "simiData_ViewField";

    private static final long serialVersionUID = -2665578122978329156L;

    @NotNull
    @Column(name = "SORT", nullable = false)
    private Integer sort = 0;

    @NotNull
    @Column(name = "WGC_EXPOSED", nullable = false)
    private Boolean wgcExposed = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TABLE_FIELD_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private TableField tableField;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TABLE_VIEW_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private TableView tableView;

    public Boolean getWgcExposed() {
        return wgcExposed;
    }

    public void setWgcExposed(Boolean wgcExposed) {
        this.wgcExposed = wgcExposed;
    }

    public TableView getTableView() {
        return tableView;
    }

    public void setTableView(TableView tableView) {
        this.tableView = tableView;
    }

    public TableField getTableField() {
        return tableField;
    }

    public void setTableField(TableField tableField) {
        this.tableField = tableField;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}