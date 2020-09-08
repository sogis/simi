package ch.so.agi.simi.entity.data.tabular;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Table(name = "SIMI_TABLE_FIELD")
@Entity(name = "simi_TableField")
@NamePattern("%s|name")
public class TableField extends StandardEntity {
    private static final long serialVersionUID = -1809352037109072642L;

    @NotNull
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
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

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    @Lob
    @Column(name = "REMARKS_MODEL")
    private String remarksModel;

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

    public String getRemarksModel() {
        return remarksModel;
    }

    public void setRemarksModel(String remarksModel) {
        this.remarksModel = remarksModel;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
}