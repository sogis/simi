package ch.so.agi.simi.entity.theme;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.theme.org.OrgUnit;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMITHEME_THEME")
@Entity(name = Theme.NAME)
@NamePattern("%s|identifier")
public class Theme extends SimiEntity {
    public static final String NAME = "simiTheme_Theme";

    private static final long serialVersionUID = -3674602553752969888L;

    @NotNull
    @Column(name = "IDENTIFIER", nullable = false, unique = true, length = 100)
    private String identifier;

    @OneToMany(mappedBy = "theme")
    @Composition
    private List<ThemePublication> themePublications;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open"})
    @NotNull
    @OnDeleteInverse(DeletePolicy.DENY)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DATA_OWNER_ID")
    private OrgUnit dataOwner;

    @NotNull
    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @NotNull
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;

    @NotNull
    @Column(name = "COVERAGE_IDENT", nullable = false, length = 100)
    private String coverageIdent;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    public List<ThemePublication> getThemePublications() {
        return themePublications;
    }

    public void setThemePublications(List<ThemePublication> themePublications) {
        this.themePublications = themePublications;
    }

    public OrgUnit getDataOwner() {
        return dataOwner;
    }

    public void setDataOwner(OrgUnit dataOwner) {
        this.dataOwner = dataOwner;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverageIdent() {
        return coverageIdent;
    }

    public void setCoverageIdent(String coverageIdent) {
        this.coverageIdent = coverageIdent;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}