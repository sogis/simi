package ch.so.agi.simi.entity.theme;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.theme.org.OrgUnit;
import ch.so.agi.simi.global.validation.JsonArrayField;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.validation.groups.UiComponentChecks;

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

    @JsonArrayField(groups = {UiComponentChecks.class})
    @Column(name = "KEYWORDS_ARR", length = 800)
    private String keywordsArr;

    @JsonArrayField(groups = {UiComponentChecks.class})
    @Column(name = "SYNONYMS_ARR", length = 800)
    private String synonymsArr;

    @Column(name = "FURTHER_INFO_URL", length = 500)
    private String furtherInfoUrl;

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

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    public String getFurtherInfoUrl() {
        return furtherInfoUrl;
    }

    public void setFurtherInfoUrl(String furtherInfoUrl) {
        this.furtherInfoUrl = furtherInfoUrl;
    }

    public String getSynonymsArr() {
        return synonymsArr;
    }

    public void setSynonymsArr(String synonymsArr) {
        this.synonymsArr = synonymsArr;
    }

    public String getKeywordsArr() {
        return keywordsArr;
    }

    public void setKeywordsArr(String keywordsArr) {
        this.keywordsArr = keywordsArr;
    }

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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}