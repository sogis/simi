package ch.so.agi.simi.entity.data;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.data.datasetview.ViewField;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Table(name = "SIMIDATA_TABLE_FIELD", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_SIMIDATA_TABLE_FIELD_UNQ_NAME_POSTGRES_TABLE_ID", columnNames = {"NAME", "POSTGRES_TABLE_ID"})
})
@Entity(name = TableField.NAME)
@NamePattern("#getCaption|name,typeName,strLength")
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

    @Column(name = "REG_EX_PATTERN", length = 512)
    private String regExPattern;

    @Column(name = "STR_LENGTH")
    @Positive
    private Integer strLength;

    @NotNull
    @Column(name = "CAT_SYNCED", nullable = false)
    private Boolean catSynced = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POSTGRES_TABLE_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private PostgresTable postgresTable;

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

    public String getRegExPattern() {
        return regExPattern;
    }

    public void setRegExPattern(String regExPattern) {
        this.regExPattern = regExPattern;
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

    public String getCaption() {
        return name + " " + typeName + (strLength == null ? "" : "(" + strLength + ")");
    }
}