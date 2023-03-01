package ch.so.agi.simi.entity.theme.dbview;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.global.DbView;
import com.haulmont.cuba.core.global.DdlGeneration;

import javax.persistence.*;

@DbView
@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.CREATE_ONLY)
@Table(name = "app_themepub_validation_v")
@Entity(name = ThemePubValidation.NAME)
@NamePattern("%s (%s)|identifier,title")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "tp_id"))
})
public class ThemePubValidation extends BaseUuidEntity {
    private static final long serialVersionUID = -3991781548381925049L;

    public static final String NAME = "simiTheme_ThemePubValidation";

    @Lob
    @Column(name = "data_owner")
    private String dataOwner;

    @Lob
    @Column(name = "data_servicer")
    private String dataServicer;

    @Lob
    @Column(name = "file_formats")
    private String fileFormats;

    @Column(name = "further_info", length = 500)
    private String furtherInfo;

    @Lob
    @Column(name = "identifier")
    private String identifier;

    @Lob
    @Column(name = "keywords")
    private String keywords;

    @Column(name = "public_model_name", length = 150)
    private String publicModelName;

    @Lob
    @Column(name = "services")
    private String services;

    @Column(name = "short_description", length = 1000)
    private String shortDescription;

    @Lob
    @Column(name = "synonyms")
    private String synonyms;

    @Lob
    @Column(name = "tables_json")
    private String tablesJson;

    @Column(name = "title", length = 200)
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTablesJson() {
        return tablesJson;
    }

    public void setTablesJson(String tablesJson) {
        this.tablesJson = tablesJson;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getPublicModelName() {
        return publicModelName;
    }

    public void setPublicModelName(String publicModelName) {
        this.publicModelName = publicModelName;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFurtherInfo() {
        return furtherInfo;
    }

    public void setFurtherInfo(String furtherInfo) {
        this.furtherInfo = furtherInfo;
    }

    public String getFileFormats() {
        return fileFormats;
    }

    public void setFileFormats(String fileFormats) {
        this.fileFormats = fileFormats;
    }

    public String getDataServicer() {
        return dataServicer;
    }

    public void setDataServicer(String dataServicer) {
        this.dataServicer = dataServicer;
    }

    public String getDataOwner() {
        return dataOwner;
    }

    public void setDataOwner(String dataOwner) {
        this.dataOwner = dataOwner;
    }
}