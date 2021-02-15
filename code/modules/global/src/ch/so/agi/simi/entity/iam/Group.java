package ch.so.agi.simi.entity.iam;

import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIIAM_GROUP")
@Entity(name = Group.NAME)
@NamePattern("%s|identifier")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
public class Group extends Identity {

    public static final String NAME = "simiIAM_Group";

    private static final long serialVersionUID = 109810719306418897L;

    @NotNull
    @Column(name = "PUB_AS_USER", nullable = false)
    private Boolean pubAsUser = false;

    @JoinTable(name = "SIMIIAM_GROUP_USER_LINK",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @ManyToMany
    private List<User> users;

    @JoinTable(name = "SIMIIAM_ROLE_GROUP_LINK",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    @ManyToMany
    private List<Role> roles;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Boolean getPubAsUser() {
        return pubAsUser;
    }

    public void setPubAsUser(Boolean pubAsUser) {
        this.pubAsUser = pubAsUser;
    }
}