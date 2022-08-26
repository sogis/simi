package ch.so.agi.simi.entity.theme.subarea;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.theme.ThemePublication_TypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "SIMITHEME_PUBLISHED_SUB_AREA_HELPER")
@Entity(name = PublishedSubAreaHelper.NAME)
public class PublishedSubAreaHelper extends SimiEntity {
    private static final long serialVersionUID = -1902340889548632093L;

    public static final String NAME = "simiTheme_PublishedSubAreaHelper";

    @NotNull
    @Column(name = "PUBLISHED", nullable = false)
    private LocalDateTime published;

    @NotNull
    @Column(name = "THEME_PUBLICATION_ID", nullable = false)
    private UUID themePublicationId;

    @NotNull
    @Column(name = "PREV_PUBLISHED", nullable = false)
    private LocalDateTime prevPublished;

    @NotNull
    @Column(name = "SUB_AREA_IDENT", nullable = false, length = 100)
    private String subAreaIdent;

    @Column(name = "THEME_PUB_DATA_CLASS_OVERRIDE", length = 50)
    private String themePubDataClassOverride;

    @NotNull
    @Column(name = "THEME_IDENTIFIER", nullable = false, length = 100)
    private String themeIdentifier;

    @NotNull
    @Column(name = "THEME_PUB_DATA_CLASS", nullable = false)
    private String themePubDataClass;

    @NotNull
    @Column(name = "SUB_AREA_ID", nullable = false)
    private UUID subAreaId;

    public void setThemePubDataClass(ThemePublication_TypeEnum themePubDataClass) {
        this.themePubDataClass = themePubDataClass == null ? null : themePubDataClass.getId();
    }

    public ThemePublication_TypeEnum getThemePubDataClass() {
        return themePubDataClass == null ? null : ThemePublication_TypeEnum.fromId(themePubDataClass);
    }

    public String getThemePubDataClassOverride() {
        return themePubDataClassOverride;
    }

    public void setThemePubDataClassOverride(String themePubDataClassOverride) {
        this.themePubDataClassOverride = themePubDataClassOverride;
    }

    public String getThemeIdentifier() {
        return themeIdentifier;
    }

    public void setThemeIdentifier(String themeIdentifier) {
        this.themeIdentifier = themeIdentifier;
    }

    public String getSubAreaIdent() {
        return subAreaIdent;
    }

    public void setSubAreaIdent(String subAreaIdent) {
        this.subAreaIdent = subAreaIdent;
    }

    public UUID getSubAreaId() {
        return subAreaId;
    }

    public void setSubAreaId(UUID subAreaId) {
        this.subAreaId = subAreaId;
    }

    public UUID getThemePublicationId() {
        return themePublicationId;
    }

    public void setThemePublicationId(UUID themePublicationId) {
        this.themePublicationId = themePublicationId;
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
}