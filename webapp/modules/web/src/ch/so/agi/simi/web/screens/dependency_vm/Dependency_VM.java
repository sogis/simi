package ch.so.agi.simi.web.screens.dependency_vm;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/*
Viewmodel to display the dependencies bottom up (Tbl -> DSV -> FL ...)
 */
@MetaClass(name = "simi_Dependency_VM")
public class Dependency_VM extends BaseUuidEntity {
    private static final long serialVersionUID = -6856171102289271460L;

    @NotNull
    @MetaProperty(mandatory = true)
    private String display;

    @NotNull
    @MetaProperty(mandatory = true)
    private String identifier;

    @MetaProperty
    private String entityIdent;

    @MetaProperty
    private UUID entityId;

    @MetaProperty
    private Dependency_VM parent;

    @NotNull
    @MetaProperty(mandatory = true)
    private String relationDescription;

    @MetaProperty
    public String getParentDisplay(){
        if(parent == null)
            return null;

        return parent.getDisplay();
    }

    public Dependency_VM getParent() {
        return parent;
    }

    public void setParent(Dependency_VM parent) {
        this.parent = parent;
    }

    public String getRelationDescription() {
        return relationDescription;
    }

    public void setRelationDescription(String relationDescription) {
        this.relationDescription = relationDescription;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public String getEntityIdent() {
        return entityIdent;
    }

    public void setEntityIdent(String entityIdent) {
        this.entityIdent = entityIdent;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}