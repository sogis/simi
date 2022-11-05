package ch.so.agi.simi.entity.theme.subarea;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.global.exc.SimiException;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "SIMITHEME_PUBLISHED_SUB_AREA", indexes = {
        @Index(name = "IDX_SIMI_PUBLISHED_SUB_AREA_UNQ", columnList = "SUB_AREA_ID, THEME_PUBLICATION_ID", unique = true)
})
@Entity(name = PublishedSubArea.NAME)
public class PublishedSubArea extends SimiEntity {
    private static final long serialVersionUID = 1178956817317419544L;

    public static final String NAME = "simiTheme_PublishedSubArea";

    @NotNull
    @Column(name = "PUBLISHED", nullable = false)
    private LocalDateTime published = LocalDateTime.now();

    @NotNull
    @Column(name = "PREV_PUBLISHED", nullable = false)
    private LocalDateTime prevPublished = LocalDateTime.now();

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUB_AREA_ID")
    private SubArea subArea;

    @Column(name = "SUB_AREA_IDENT", length = 100)
    private String subAreaIdent;

    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "THEME_PUBLICATION_ID")
    private ThemePublication themePublication;

    public String deferSubAreaIdent() {
        if(subArea == null)
            throw new SimiException("Cant defer as linked subarea is not loaded");

        return subArea.getIdentifier();
    }

    public String getSubAreaIdent() {
        return subAreaIdent;
    }

    public void setSubAreaIdent(String subAreaIdent) {
        this.subAreaIdent = subAreaIdent;
    }

    public SubArea getSubArea() {
        return subArea;
    }

    public void setSubArea(SubArea subArea) {
        this.subArea = subArea;
    }

    public ThemePublication getThemePublication() {
        return themePublication;
    }

    public void setThemePublication(ThemePublication themePublication) {
        this.themePublication = themePublication;
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