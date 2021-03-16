package ch.so.agi.simi.entity.extended;

import javax.persistence.Entity;

@Entity(name = Report.NAME)
public class Report extends Dependency {

    public static final String NAME = "simiExtended_Report";
    private static final long serialVersionUID = -8588876548160150650L;

    @Override
    protected String typeAbbreviation(){
        return "Report";
    }
}
