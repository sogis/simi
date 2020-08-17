package ch.so.agi.simi.entity.featureinfo;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Table(name = "FI_FEATURE_INFO")
@Entity(name = "fi_FeatureInfo")
@NamePattern("%s|t_dsvName")
public class FeatureInfo extends StandardEntity {
    private static final long serialVersionUID = 569650397231873442L;

    @Lob
    @Column(name = "DISPLAY_TEMPLATE", nullable = false)
    @Length(message = "{msg://fi_FeatureInfo.displayTemplate.validation.Length}", min = 0, max = 60000)
    private String displayTemplate;

    @Lob
    @Length(message = "{msg://fi_FeatureInfo.displayTemplate.validation.Length}", min = 0, max = 60000)
    @Column(name = "SQL_QUERY")
    private String sqlQuery;

    @Column(name = "PY_MODULE_NAME", length = 100)
    private String pyModuleName;

    @Lob
    @Length(message = "{msg://fi_FeatureInfo.displayTemplate.validation.Length}", min = 0, max = 60000)
    @Column(name = "REMARKS")
    private String remarks;

    @Transient
    @MetaProperty
    private String t_dsvName;

    public String getT_dsvName() {
        return t_dsvName;
    }

    public void setT_dsvName(String t_dsvName) {
        this.t_dsvName = t_dsvName;
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