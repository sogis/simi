package ch.so.agi.simi.entity.iam;

import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMI_USER")
@Entity(name = "simi_User")
@NamePattern("%s (%s %s)|identifier,firstname,lastname")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
public class User extends Identity {
    private static final long serialVersionUID = -2918662812811270888L;

    @NotNull
    @Column(name = "FIRSTNAME", nullable = false, length = 100)
    private String firstname;

    @NotNull
    @Column(name = "LASTNAME", nullable = false, length = 100)
    private String lastname;

    @JoinTable(name = "SIMI_GROUP_USER_LINK",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
    @ManyToMany
    private List<Group> groups;

    @JoinTable(name = "SIMI_ROLE_USER_LINK",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    @ManyToMany
    private List<Role> roles;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}