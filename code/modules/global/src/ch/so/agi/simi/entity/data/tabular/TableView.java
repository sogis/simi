package ch.so.agi.simi.entity.data.tabular;

import ch.so.agi.simi.entity.featureinfo.DataSetView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "simi_TableView")
public class TableView extends DataSetView {
    private static final long serialVersionUID = -4901858225372396346L;

    @Column(name = "WHERE_CLAUSE", length = 200)
    private String whereClause;

    @Column(name = "GEOM_FIELD_NAME", length = 100)
    private String geomFieldName;

    @NotNull
    @Column(name = "WGC_EDIT", nullable = false)
    private Boolean wgcEdit = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POSTGRES_TABLE_ID")
    private PostgresTable postgresTable;

    public PostgresTable getPostgresTable() {
        return postgresTable;
    }

    public void setPostgresTable(PostgresTable postgresTable) {
        this.postgresTable = postgresTable;
    }

    public Boolean getWgcEdit() {
        return wgcEdit;
    }

    public void setWgcEdit(Boolean wgcEdit) {
        this.wgcEdit = wgcEdit;
    }

    public String getGeomFieldName() {
        return geomFieldName;
    }

    public void setGeomFieldName(String geomFieldName) {
        this.geomFieldName = geomFieldName;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }
}