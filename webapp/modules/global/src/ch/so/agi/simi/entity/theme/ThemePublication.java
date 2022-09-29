package ch.so.agi.simi.entity.theme;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.product.DataProduct;
import ch.so.agi.simi.entity.theme.subarea.PublishedSubArea;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "SIMITHEME_THEME_PUBLICATION", indexes = {
        @Index(name = "IDX_SIMI_THEME_PUBLICATION_UNQ", columnList = "THEME_ID, CLASS_SUFFIX_OVERRIDE", unique = true)
})
@Entity(name = ThemePublication.NAME)
@NamePattern("#deferFullIdent|classSuffixOverride,theme")
public class ThemePublication extends SimiEntity {
    public static final String NAME = "simiTheme_ThemePublication";

    private static final long serialVersionUID = -1845966565955648130L;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open"})
    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "THEME_ID")
    private Theme theme;

    @Column(name = "MODEL_UPDATE_TS")
    private LocalDateTime modelUpdateTs;

    @Column(name = "MODEL_UPDATED_BY", length = 50)
    private String modelUpdatedBy;

    @Column(name = "PUBLIC_MODEL_NAME", length = 150)
    private String publicModelName;

    @Column(name = "DESCRIPTION_OVERRIDE", length = 1000)
    private String descriptionOverride;

    @NotNull
    @Column(name = "COVERAGE_IDENT", nullable = false, length = 100)
    private String coverageIdent = "av_kt";

    @Composition
    @OneToMany(mappedBy = "themePublication")
    private List<DataProduct> dataProducts;

    @NotNull
    @Column(name = "DATA_CLASS", nullable = false)
    private String dataClass;

    @Column(name = "CLASS_SUFFIX_OVERRIDE", length = 50)
    private String classSuffixOverride;

    @Column(name = "TITLE_OVERRIDE", length = 200)
    private String titleOverride;

    @JoinTable(name = "SIMITHEME_THEME_PUBLICATION_CUSTOM_FILE_TYPE_LINK",
            joinColumns = @JoinColumn(name = "THEME_PUBLICATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "CUSTOM_FILE_TYPE_ID"))
    @ManyToMany
    private List<CustomFileType> customFileTypes;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    @OneToMany(mappedBy = "themePublication")
    private List<PublishedSubArea> publishedSubAreas;

    public String deferFullIdent(){
        String res = "";

        if(theme != null)
            res = theme.getIdentifier();

        String suffix = classSuffixOverride;
        if(suffix != null)
            res += "." + suffix;

        return res;
    }

    @MetaProperty
    public String getDerivedIdent(){
        return deferFullIdent();
    }

    private String deferSuffix(){
        String suffix = null;

        ThemePublication_TypeEnum dataClassEnum = ThemePublication_TypeEnum.fromId(dataClass);
        if(dataClassEnum == null)
            return null;

        if(classSuffixOverride != null)
            return classSuffixOverride;

        switch (dataClassEnum){
            case TABLE_RELATIONAL:
                suffix = "relational";
                break;
            case TABLE_SIMPLE:
                break;
            case NON_TABULAR:
                break;
            default:
                suffix = "err:suffix_not_set";
                break;
        }

        return suffix;
    }

    public String getClassSuffixOverride() {
        return classSuffixOverride;
    }

    public void setClassSuffixOverride(String classSuffixOverride) {
        this.classSuffixOverride = classSuffixOverride;
    }

    public String getModelUpdatedBy() {
        return modelUpdatedBy;
    }

    public void setModelUpdatedBy(String modelUpdatedBy) {
        this.modelUpdatedBy = modelUpdatedBy;
    }

    public LocalDateTime getModelUpdateTs() {
        return modelUpdateTs;
    }

    public void setModelUpdateTs(LocalDateTime modelUpdateTs) {
        this.modelUpdateTs = modelUpdateTs;
    }

    public String getPublicModelName() {
        return publicModelName;
    }

    public void setPublicModelName(String publicModelName) {
        this.publicModelName = publicModelName;
    }

    public String getDescriptionOverride() {
        return descriptionOverride;
    }

    public void setDescriptionOverride(String descriptionOverride) {
        this.descriptionOverride = descriptionOverride;
    }

    public List<PublishedSubArea> getPublishedSubAreas() {
        return publishedSubAreas;
    }

    public void setPublishedSubAreas(List<PublishedSubArea> publishedSubAreas) {
        this.publishedSubAreas = publishedSubAreas;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTitleOverride() {
        return titleOverride;
    }

    public void setTitleOverride(String titleOverride) {
        this.titleOverride = titleOverride;
    }

    public String getCoverageIdent() {
        return coverageIdent;
    }

    public void setCoverageIdent(String coverageIdent) {
        this.coverageIdent = coverageIdent;
    }

    public List<DataProduct> getDataProducts() {
        return dataProducts;
    }

    public void setDataProducts(List<DataProduct> dataProducts) {
        this.dataProducts = dataProducts;
    }

    public List<CustomFileType> getCustomFileTypes() {
        return customFileTypes;
    }

    public void setCustomFileTypes(List<CustomFileType> customFileTypes) {
        this.customFileTypes = customFileTypes;
    }

    public ThemePublication_TypeEnum getDataClass() {
        return dataClass == null ? null : ThemePublication_TypeEnum.fromId(dataClass);
    }

    public void setDataClass(ThemePublication_TypeEnum dataClass) {
        this.dataClass = dataClass == null ? null : dataClass.getId();
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}