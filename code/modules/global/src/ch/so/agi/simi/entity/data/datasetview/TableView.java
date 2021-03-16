package ch.so.agi.simi.entity.data.datasetview;

import ch.so.agi.simi.entity.data.PostgresTable;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIDATA_TABLE_VIEW")
@Entity(name = TableView.NAME)
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("#concatName|identifier,title") //needed to define minimal view
public class TableView extends DataSetView {

    public static final String NAME = "simiData_TableView";

    private static final long serialVersionUID = -4901858225372396346L;

    @Column(name = "WHERE_CLAUSE", length = 200)
    private String whereClause;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "tableView")
    @OrderBy("sort")
    private List<ViewField> viewFields;

    @Column(name = "GEOM_FIELD_NAME", length = 100)
    private String geomFieldName;

    @Column(name = "GEO_TYPE", length = 100)
    private String geoType;

    @Column(name = "GEO_EPSG_CODE")
    private Integer geoEpsgCode;

    @NotNull
    @Column(name = "WGC_EDIT", nullable = false)
    private Boolean wgcEdit = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POSTGRES_TABLE_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private PostgresTable postgresTable;

    public Integer getGeoEpsgCode() {
        return geoEpsgCode;
    }

    public void setGeoEpsgCode(Integer geoEpsgCode) {
        this.geoEpsgCode = geoEpsgCode;
    }

    public String getGeoType() {
        return geoType;
    }

    public void setGeoType(String geoType) {
        this.geoType = geoType;
    }

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