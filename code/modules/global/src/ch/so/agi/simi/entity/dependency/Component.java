package ch.so.agi.simi.entity.dependency;

import javax.persistence.Entity;

@Entity(name = Component.NAME)
public class Component extends Dependency {

    public static final String NAME = "simiDependency_Component";
    private static final long serialVersionUID = -4862112351147530759L;

    @Override
    protected String typeAbbreviation(){
        return "Modul";
    }
}