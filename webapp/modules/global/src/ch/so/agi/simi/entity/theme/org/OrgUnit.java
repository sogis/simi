package ch.so.agi.simi.entity.theme.org;

import ch.so.agi.simi.entity.SimiEntity;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMITHEME_ORG_UNIT")
@Entity(name = OrgUnit.NAME)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
@NamePattern("%s|name")
public class OrgUnit extends SimiEntity {

    public static final String NAME = "simiTheme_OrgUnit";

    private static final long serialVersionUID = -1262849998565786603L;

    @NotNull
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}