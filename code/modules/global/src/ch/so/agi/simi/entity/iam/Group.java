package ch.so.agi.simi.entity.iam;

import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity(name = "simi_Group")
@NamePattern("%s|identifier")
public class Group extends Identity {
    private static final long serialVersionUID = 109810719306418897L;

    @NotNull
    @Column(name = "PUB_AS_USER", nullable = false)
    private Boolean pubAsUser = false;

    @JoinTable(name = "SIMI_GROUP_USER_LINK",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @ManyToMany
    private List<User> users;

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