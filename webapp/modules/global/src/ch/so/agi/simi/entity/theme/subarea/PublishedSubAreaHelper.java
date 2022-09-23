package ch.so.agi.simi.entity.theme.subarea;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.theme.ThemePublication_TypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "SIMITHEME_PUBLISHED_SUB_AREA_HELPER")
@Entity(name = PublishedSubAreaHelper.NAME)
public class PublishedSubAreaHelper extends SimiEntity {
    private static final long serialVersionUID = -1902340889548632093L;

    public static final String NAME = "simiTheme_PublishedSubAreaHelper";

    @NotNull
    @Column(name = "PUBLISHED", nullable = false)
    private LocalDateTime published;

    @NotNull
    @Column(name = "PREV_PUBLISHED", nullable = false)
    private LocalDateTime prevPublished;

    @NotNull
    @Column(name = "TPUB_DATA_CLASS", nullable = false)
    private String tpubDataClass;

    @Column(name = "TPUB_CLASS_SUFFIX_OVERRIDE", length = 50)
    private String tpubClassSuffixOverride;

    @NotNull
    @Column(name = "SUBAREA_IDENTIFIER", nullable = false, length = 100)
    private String subareaIdentifier;

    @NotNull
    @Column(name = "SUBAREA_COVERAGE_IDENT", nullable = false, length = 100)
    private String subareaCoverageIdent;

    @NotNull
    @Column(name = "THEME_IDENTIFIER", nullable = false, length = 100)
    private String themeIdentifier;

    public String getTpubClassSuffixOverride() {
        return tpubClassSuffixOverride;
    }

    public void setTpubClassSuffixOverride(String tpubClassSuffixOverride) {
        this.tpubClassSuffixOverride = tpubClassSuffixOverride;
    }

    public ThemePublication_TypeEnum getTpubDataClass() {
        return tpubDataClass == null ? null : ThemePublication_TypeEnum.fromId(tpubDataClass);
    }

    public void setTpubDataClass(ThemePublication_TypeEnum tpubDataClass) {
        this.tpubDataClass = tpubDataClass == null ? null : tpubDataClass.getId();
    }

    public String getThemeIdentifier() {
        return themeIdentifier;
    }

    public void setThemeIdentifier(String themeIdentifier) {
        this.themeIdentifier = themeIdentifier;
    }

    public LocalDateTime getPrevPublished() {
        return prevPublished;
    }

    public void setPrevPublished(LocalDateTime prevPublished) {
        this.prevPublished = prevPublished;
    }

    public LocalDateTime getPublished() {
        return published;
    }

    public void setPublished(LocalDateTime published) {
        this.published = published;
    }

    public String getSubareaIdentifier() {
        return subareaIdentifier;
    }

    public void setSubareaIdentifier(String subareaIdentifier) {
        this.subareaIdentifier = subareaIdentifier;
    }

    public String getSubareaCoverageIdent() {
        return subareaCoverageIdent;
    }

    public void setSubareaCoverageIdent(String subareaCoverageIdent) {
        this.subareaCoverageIdent = subareaCoverageIdent;
    }
}