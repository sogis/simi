package ch.so.agi.simi.entity.dependency;

import javax.persistence.Entity;

@Entity(name = "simiDependency_Report")
public class Report extends Dependency {
    private static final long serialVersionUID = -8588876548160150650L;

    @Override
    protected String typeAbbreviation(){
        return "Report";
    }
}
