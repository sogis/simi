package ch.so.agi.simi.entity.featureinfo;

import ch.so.agi.simi.entity.SimiStandardEntity;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIFEATUREINFO_FEATURE_INFO")
@Entity(name = "simiFeatureInfo_FeatureInfo")
public class FeatureInfo extends SimiStandardEntity {
    private static final long serialVersionUID = 3002954753875689457L;

    @NotNull
    @Lob
    @Column(name = "DISPLAY_TEMPLATE", nullable = false)
    private String displayTemplate;

    @Lob
    @Column(name = "SQL_QUERY")
    private String sqlQuery;

    @Column(name = "PY_MODULE_NAME", length = 100)
    private String pyModuleName;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "featureInfo")
    private List<LayerRelation> layerRelation;

    public List<LayerRelation> getLayerRelation() {
        return layerRelation;
    }

    public void setLayerRelation(List<LayerRelation> layerRelation) {
        this.layerRelation = layerRelation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPyModuleName() {
        return pyModuleName;
    }

    public void setPyModuleName(String pyModuleName) {
        this.pyModuleName = pyModuleName;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getDisplayTemplate() {
        return displayTemplate;
    }

    public void setDisplayTemplate(String displayTemplate) {
        this.displayTemplate = displayTemplate;
    }
}