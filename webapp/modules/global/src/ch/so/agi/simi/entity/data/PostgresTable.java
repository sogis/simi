package ch.so.agi.simi.entity.data;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.data.datasetview.TableView;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "SIMIDATA_POSTGRES_TABLE", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_SIMI_POSTGRES_TABLE_UNQ", columnNames = {"TABLE_NAME", "DB_SCHEMA_ID"})
})
@Entity(name = PostgresTable.NAME)
@NamePattern("#concatName|tableName")
public class PostgresTable extends SimiEntity {

    public static final String NAME = "simiData_PostgresTable";

    private static final long serialVersionUID = -6324189221967537598L;

    @Column(name = "ID_FIELD_NAME", nullable = false, length = 100)
    @NotNull
    private String idFieldName;

    @Column(name = "KEYWORDS_ARR", length = 800)
    private String keywordsArr;

    @Column(name = "SYNONYMS_ARR", length = 800)
    private String synonymsArr;

    @NotNull
    @Column(name = "TABLE_IS_VIEW", nullable = false)
    private Boolean tableIsView = false;

    @NotNull
    @Column(name = "TITLE", nullable = false)
    private String title;

    @Lob
    @Column(name = "DESCRIPTION_OVERRIDE")
    private String descriptionOverride;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DB_SCHEMA_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private DbSchema dbSchema;

    @OneToMany(mappedBy = "postgresTable")
    @Composition
    private List<TableView> tableViews;

    @Composition
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

    @Column(name = "TABLE_NAME", nullable = false, length = 100)
    @NotNull
    private String tableName;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    public String getSynonymsArr() {
        return synonymsArr;
    }

    public void setSynonymsArr(String synonymsArr) {
        this.synonymsArr = synonymsArr;
    }

    public String getKeywordsArr() {
        return keywordsArr;
    }

    public void setKeywordsArr(String keywordsArr) {
        this.keywordsArr = keywordsArr;
    }

    public String getDescriptionOverride() {
        return descriptionOverride;
    }

    public void setDescriptionOverride(String descriptionOverride) {
        this.descriptionOverride = descriptionOverride;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getTableIsView() {
        return tableIsView;
    }

    public void setTableIsView(Boolean tableIsView) {
        this.tableIsView = tableIsView;
    }

    public DbSchema getDbSchema() {
        return dbSchema;
    }

    public void setDbSchema(DbSchema dbSchema) {
        this.dbSchema = dbSchema;
    }

    public String concatName(){
        return this.getDbSchema().concatName() + " | " + this.getTableName() ;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

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