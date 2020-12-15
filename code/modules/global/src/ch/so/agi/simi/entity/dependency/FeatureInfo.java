package ch.so.agi.simi.entity.dependency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Entity(name = "simiDependency_FeatureInfo")
public class FeatureInfo extends Dependency {
    private static final long serialVersionUID = 449117253137188881L;

    @NotNull
    @Lob
    @Column(name = "DISPLAY_TEMPLATE", nullable = false)
    private String displayTemplate;

    @Lob
    @Column(name = "SQL_QUERY")
    private String sqlQuery;

    @Column(name = "PY_MODULE_NAME", length = 100)
    private String pyModuleName;

    @Override
    protected String typeAbbreviation(){
        return "FeatureInfo";
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