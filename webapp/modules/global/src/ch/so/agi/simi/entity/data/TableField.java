package ch.so.agi.simi.entity.data;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.data.datasetview.ViewField;
import ch.so.agi.simi.global.validation.JsonArrayField;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.validation.groups.UiComponentChecks;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Table(name = "SIMIDATA_TABLE_FIELD", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_SIMIDATA_TABLE_FIELD_UNQ_NAME_POSTGRES_TABLE_ID", columnNames = {"NAME", "POSTGRES_TABLE_ID"})
})
@Entity(name = TableField.NAME)
@NamePattern("#getCaption|name,alias")
public class TableField extends SimiEntity {

    public static final String NAME = "simiData_TableField";

    private static final long serialVersionUID = -1809352037109072642L;

    @NotNull
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Lob
    @Column(name = "DESCRIPTION_MODEL")
    private String descriptionModel;

    @Lob
    @Column(name = "DESCRIPTION_OVERRIDE")
    private String descriptionOverride;

    @Composition
    @OneToMany(mappedBy = "tableField")
    private List<ViewField> viewFields;

    @NotNull
    @Column(name = "TYPE_NAME", nullable = false, length = 100)
    private String typeName;

    @NotNull
    @Column(name = "MANDATORY", nullable = false)
    private Boolean mandatory = false;

    @Column(name = "STR_LENGTH")
    @Positive
    private Integer strLength;

    @NotNull
    @Column(name = "CAT_SYNCED", nullable = false)
    private Boolean catSynced = false;

    @NotNull
    @Column(name = "ILI_ENUM", nullable = false)
    private Boolean iliEnum = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POSTGRES_TABLE_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private PostgresTable postgresTable;

    @Column(name = "ALIAS", length = 100)
    private String alias;

    @Column(name = "WMS_FI_FORMAT", length = 100)
    private String wmsFiFormat;

    @JsonArrayField(groups = {UiComponentChecks.class})
    @Lob
    @Column(name = "DISPLAY_PROPS4_JSON")
    private String displayProps4Json;

    public Boolean getIliEnum() {
        return iliEnum;
    }

    public void setIliEnum(Boolean iliEnum) {
        this.iliEnum = iliEnum;
    }

    public String getDescriptionOverride() {
        return descriptionOverride;
    }

    public void setDescriptionOverride(String descriptionOverride) {
        this.descriptionOverride = descriptionOverride;
    }

    public String getDescriptionModel() {
        return descriptionModel;
    }

    public void setDescriptionModel(String descriptionModel) {
        this.descriptionModel = descriptionModel;
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

    public Boolean getCatSynced() {
        return catSynced;
    }

    public void setCatSynced(Boolean catSynced) {
        this.catSynced = catSynced;
    }

    public Integer getStrLength() {
        return strLength;
    }

    public void setStrLength(Integer strLength) {
        this.strLength = strLength;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCaption() {
        return name + " (" + alias + ")";
    }
}