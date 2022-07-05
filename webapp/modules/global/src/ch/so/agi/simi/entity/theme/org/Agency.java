package ch.so.agi.simi.entity.theme.org;

import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "SIMITHEME_AGENCY")
@Entity(name = "simiTheme_Agency")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
public class Agency extends OrgUnit {
    public static final String NAME = "simiTheme_Agency";

    private static final long serialVersionUID = 8984530605056234003L;

    @NotNull
    @Column(name = "ABBREVIATION", nullable = false, unique = true, length = 10)
    private String abbreviation;

    @NotNull
    @Column(name = "URL", nullable = false)
    private String url;

    @NotNull
    @Column(name = "PHONE", nullable = false, length = 20)
    private String phone;

    @NotNull
    @Column(name = "EMAIL", nullable = false, length = 50)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

}