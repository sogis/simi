package ch.so.agi.simi.entity.data.tabular;

import ch.so.agi.simi.entity.product.DataSetView;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMI_TABLE_VIEW")
@Entity(name = "simi_TableView")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("%s (%s)|identifier,postgresTable")
public class TableView extends DataSetView {
    private static final long serialVersionUID = -4901858225372396346L;

    @Column(name = "WHERE_CLAUSE", length = 200)
    private String whereClause;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "tableView")
    private List<ViewField> viewFields;

    @Column(name = "GEOM_FIELD_NAME", length = 100)
    private String geomFieldName;

    @NotNull
    @Column(name = "WGC_EDIT", nullable = false)
    private Boolean wgcEdit = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POSTGRES_TABLE_ID")
    private PostgresTable postgresTable;

    public List<ViewField> getViewFields() {
        return viewFields;
    }

    public void setViewFields(List<ViewField> viewFields) {
        this.viewFields = viewFields;
    }

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