package ch.so.agi.simi.entity.dependency;

import javax.persistence.Entity;

@Entity(name = "simiDependency_Component")
public class Component extends Dependency {
    private static final long serialVersionUID = -4862112351147530759L;

    @Override
    protected String typeAbbreviation(){
        return "Modul";
    }
}