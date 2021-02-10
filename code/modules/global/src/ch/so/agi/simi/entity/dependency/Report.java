package ch.so.agi.simi.entity.dependency;

import javax.persistence.Entity;

@Entity(name = Report.NAME)
public class Report extends Dependency {

    public static final String NAME = "simiDependency_Report";
    private static final long serialVersionUID = -8588876548160150650L;

    @Override
    protected String typeAbbreviation(){
        return "Report";
    }
}
