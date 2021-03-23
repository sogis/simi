package ch.so.agi.simi.entity.data;

import ch.so.agi.simi.entity.data.datasetview.TableView;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "SIMIDATA_POSTGRES_TABLE")
@Entity(name = PostgresTable.NAME)
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("%s.%s|dataTheme,tableName")
public class PostgresTable extends TableDS {

    public static final String NAME = "simiData_PostgresTable";

    private static final long serialVersionUID = -6324189221967537598L;

    @Column(name = "ID_FIELD_NAME", nullable = false, length = 100)
    @NotNull
    private String idFieldName;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DATA_THEME_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private DataTheme dataTheme;

    @OneToMany(mappedBy = "postgresTable")
    @Composition
    private List<TableView> tableViews;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "postgresTable")
    @OrderBy("name")
    private List<TableField> tableFields;

    @Lob
    @Column(name = "DESCRIPTION_MODEL")
    private String descriptionModel;

    @NotNull
    @Column(name = "CAT_SYNC_STAMP", nullable = false)
    private LocalDateTime catSyncStamp;

    @Column(name = "GEO_FIELD_NAME", length = 100)
    private String geoFieldName;

    @Column(name = "GEO_TYPE", length = 100)
    private String geoType;

    @Column(name = "GEO_EPSG_CODE")
    private Integer geoEpsgCode;

    public List<TableField> getTableFields() {
        return tableFields;
    }

    public void setTableFields(List<TableField> tableFields) {
        this.tableFields = tableFields;
    }

    public List<TableView> getTableViews() {
        return tableViews;
    }

    public void setTableViews(List<TableView> tableViews) {
        this.tableViews = tableViews;
    }

    public DataTheme getDataTheme() {
        return dataTheme;
    }

    public void setDataTheme(DataTheme dataTheme) {
        this.dataTheme = dataTheme;
    }

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

    public String getGeoFieldName() {
        return geoFieldName;
    }

    public void setGeoFieldName(String geoFieldName) {
        this.geoFieldName = geoFieldName;
    }

    public LocalDateTime getCatSyncStamp() {
        return catSyncStamp;
    }

    public void setCatSyncStamp(LocalDateTime catSyncStamp) {
        this.catSyncStamp = catSyncStamp;
    }

    public String getDescriptionModel() {
        return descriptionModel;
    }

    public void setDescriptionModel(String descriptionModel) {
        this.descriptionModel = descriptionModel;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public TableField findField(String fieldName){
        if(fieldName == null || fieldName.length() == 0)
            throw new IllegalArgumentException("fieldName must be non empty String");

        if(tableFields == null)
            return null;

        TableField res = null;

        for(TableField tf : tableFields){
            if(fieldName.equalsIgnoreCase(tf.getName())){
                res = tf;
                break;
            }
        }

        return res;
    }
}