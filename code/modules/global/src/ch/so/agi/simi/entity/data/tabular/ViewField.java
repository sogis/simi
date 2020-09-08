package ch.so.agi.simi.entity.data.tabular;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMI_VIEW_FIELD")
@Entity(name = "simi_ViewField")
public class ViewField extends StandardEntity {
    private static final long serialVersionUID = -2665578122978329156L;

    @NotNull
    @Column(name = "SORT", nullable = false)
    private Integer sort;

    @Column(name = "ALIAS", length = 100)
    private String alias;

    @Column(name = "WMS_FI_FORMAT", length = 100)
    private String wmsFiFormat;

    @Lob
    @Column(name = "DISPLAY_PROPS4_JSON")
    private String displayProps4Json;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TABLE_FIELD_ID")
    private TableField tableField;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TABLE_VIEW_ID")
    private TableView tableView;

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

    public String getDisplayProps4Json() {
        return displayProps4Json;
    }

    public void setDisplayProps4Json(String displayProps4Json) {
        this.displayProps4Json = displayProps4Json;
    }

    public String getWmsFiFormat() {
        return wmsFiFormat;
    }

    public void setWmsFiFormat(String wmsFiFormat) {
        this.wmsFiFormat = wmsFiFormat;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}