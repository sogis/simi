package ch.so.agi.simi.entity.extended;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Entity(name = FeatureInfo.NAME)
public class FeatureInfo extends Dependency {

    public static final String NAME = "simiExtended_FeatureInfo";
    private static final long serialVersionUID = 449117253137188881L;

    @Lob
    @Column(name = "DISPLAY_TEMPLATE")
    @NotNull
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