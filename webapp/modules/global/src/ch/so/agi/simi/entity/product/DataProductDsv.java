package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.DbView;
import com.haulmont.cuba.core.global.DdlGeneration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@DbView
@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.CREATE_ONLY)
@Table(name = "app_theme_dprod_v")
@Entity(name = DataProductDsv.NAME)
@NamePattern("%s|title")
public class DataProductDsv extends BaseUuidEntity {
    private static final long serialVersionUID = -6189034884806619544L;
    public static final String NAME = "simiProduct_DataProductDsv";

    @Column(name = "derived_identifier", length = 100)
    private String derivedIdentifier;
    @Column(name = "is_file_download_dsv")
    private Boolean isFileDownloadDsv;
    @Column(name = "theme_only_for_org")
    private Boolean themeOnlyForOrg;
    @Column(name = "theme_publication_id")
    private UUID themePublication;
    @Column(name = "title", length = 200)
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getThemePublication() {
        return themePublication;
    }

    public void setThemePublication(UUID themePublication) {
        this.themePublication = themePublication;
    }

    public Boolean getThemeOnlyForOrg() {
        return themeOnlyForOrg;
    }

    public void setThemeOnlyForOrg(Boolean themeOnlyForOrg) {
        this.themeOnlyForOrg = themeOnlyForOrg;
    }

    public Boolean getIsFileDownloadDsv() {
        return isFileDownloadDsv;
    }

    public void setIsFileDownloadDsv(Boolean isFileDownloadDsv) {
        this.isFileDownloadDsv = isFileDownloadDsv;
    }

    public String getDerivedIdentifier() {
        return derivedIdentifier;
    }

    public void setDerivedIdentifier(String derivedIdentifier) {
        this.derivedIdentifier = derivedIdentifier;
    }
}