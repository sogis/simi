package ch.so.agi.simi.entity.dependency;

import ch.so.agi.simi.entity.SimiEntity;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.MetaProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIDEPENDENCY_DEPENDENCY")
@Entity(name = Dependency.NAME)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
public class Dependency extends SimiEntity {

    public static final String NAME = "simiDependency_Dependency";

    private static final long serialVersionUID = -8171356992105401614L;

    @Composition
    @OneToMany(mappedBy = "dependency")
    private List<Relation> relations;

    @NotNull
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @MetaProperty
    public String getTypeAbbreviation() { // For use in Tables, can be referenced as typeAbbreviation
        return typeAbbreviation();
    }

    protected String typeAbbreviation(){
        return "WARNING: override missing.";
    }

    @MetaProperty
    public String getSort() { // For use in Tables, can be referenced as typeAbbreviation
        return typeAbbreviation() + name;
    }
}