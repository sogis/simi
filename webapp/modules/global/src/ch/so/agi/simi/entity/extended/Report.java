package ch.so.agi.simi.entity.extended;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity(name = Report.NAME)
public class Report extends Dependency {

    public static final String NAME = "simiExtended_Report";

    private static final long serialVersionUID = -8588876548160150650L;

    @Column(name = "REP_DATASOURCE")
    @NotNull(message = "Datenquelle darf nicht null sein.")
    private String repDatasource;

    @Column(name = "REP_TABLE")
    @NotNull(message = "Tabelle darf nicht null sein.")
    private String repTable;

    @Column(name = "REP_UNIQUE_KEY")
    @NotNull(message = "Unique Key darf nicht null sein.")
    private String repUniqueKey;

    @Column(name = "REP_PARAMETER_NAME")
    @NotNull(message = "'Para. Name' darf nicht null sein.")
    private String repParameterName;

    public String getRepTable() {
        return repTable;
    }

    public void setRepTable(String repTable) {
        this.repTable = repTable;
    }

    public String getRepUniqueKey() {
        return repUniqueKey;
    }

    public void setRepUniqueKey(String repUniqueKey) {
        this.repUniqueKey = repUniqueKey;
    }

    public String getRepParameterName() {
        return repParameterName;
    }

    public void setRepParameterName(String repParameterName) {
        this.repParameterName = repParameterName;
    }

    public String getRepDatasource() {
        return repDatasource;
    }

    public void setRepDatasource(String repDatasource) {
        this.repDatasource = repDatasource;
    }

    public String getParameterName() {
        return repParameterName;
    }

    public void setParameterName(String parameterName) {
        this.repParameterName = parameterName;
    }

    @Override
    protected String typeAbbreviation(){
        return "Report";
    }
}
