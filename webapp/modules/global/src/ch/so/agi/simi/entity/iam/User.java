package ch.so.agi.simi.entity.iam;

import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import java.util.List;

@Table(name = "SIMIIAM_USER")
@Entity(name = User.NAME)
@NamePattern("%s ( )|identifier")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
public class User extends Identity {

    public static final String NAME = "simiIAM_User";

    private static final long serialVersionUID = -2918662812811270888L;

    @JoinTable(name = "SIMIIAM_GROUP_USER_LINK",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
    @ManyToMany
    private List<Group> groups;

    @JoinTable(name = "SIMIIAM_ROLE_USER_LINK",
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

}