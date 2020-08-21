package ch.so.agi.simi.entity.iam;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMI_ROLE")
@Entity(name = "simi_Role")
@NamePattern("%s|name")
public class Role extends StandardEntity {
    private static final long serialVersionUID = -1282147615227159232L;

    @NotNull
    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    private String name;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    @JoinTable(name = "SIMI_ROLE_IDENTITY_LINK",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "IDENTITY_ID"))
    @ManyToMany
    private List<Identity> identities;

    public List<Identity> getIdentities() {
        return identities;
    }

    public void setIdentities(List<Identity> identities) {
        this.identities = identities;
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
}