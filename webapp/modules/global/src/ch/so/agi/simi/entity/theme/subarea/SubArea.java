package ch.so.agi.simi.entity.theme.subarea;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "SIMITHEME_SUB_AREA", indexes = {
        @Index(name = "IDX_SIMI_SUB_AREA_UNQ", columnList = "IDENTIFIER, COVERAGE_IDENT", unique = true)
})
@Entity(name = SubArea.NAME)
@NamePattern("%s (%s)|identifier,coverageIdent")
public class SubArea extends BaseUuidEntity {
    private static final long serialVersionUID = -2392977370805280401L;
    public static final String NAME = "simiTheme_SubArea";

    public static final String KTSO_SUBAREA_IDENTIFIER = "so";
    public static final String KTSO_COVERAGE_IDENTIFIER = "kanton";

    @NotNull
    @Column(name = "IDENTIFIER", nullable = false, length = 100)
    private String identifier;

    @NotNull
    @Column(name = "GEOM_WKB", nullable = false)
    private byte[] geomWKB;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "UPDATED")
    private LocalDateTime updated;

    @NotNull
    @Column(name = "COVERAGE_IDENT", nullable = false, length = 100)
    private String coverageIdent;

    public String getCoverageIdent() {
        return coverageIdent;
    }

    public void setCoverageIdent(String coverageIdent) {
        this.coverageIdent = coverageIdent;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getGeomWKB() {
        return geomWKB;
    }

    public void setGeomWKB(byte[] geomWKB) {
        this.geomWKB = geomWKB;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}