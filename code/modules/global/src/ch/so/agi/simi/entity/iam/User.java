package ch.so.agi.simi.entity.iam;

import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMI_USER")
@Entity(name = "simi_User")
@NamePattern("%s (%s %s)|identifier,nachname,vorname")
@PrimaryKeyJoinColumn(name = "ID")
public class User extends Identity {
    private static final long serialVersionUID = -2918662812811270888L;

    @NotNull
    @Column(name = "NACHNAME", nullable = false, length = 100)
    private String nachname;

    @NotNull
    @Column(name = "VORNAME", nullable = false, length = 100)
    private String vorname;

    @JoinTable(name = "SIMI_GROUP_USER_LINK",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
    @ManyToMany
    private List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
}