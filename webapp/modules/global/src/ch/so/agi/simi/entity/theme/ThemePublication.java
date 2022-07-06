package ch.so.agi.simi.entity.theme;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.product.DataProduct;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMITHEME_THEME_PUBLICATION")
@Entity(name = ThemePublication.NAME)
@NamePattern("#deferFullIdent|classSuffixOverride,dataClass,theme")
public class ThemePublication extends SimiEntity {
    public static final String NAME = "simiTheme_ThemePublication";

    private static final long serialVersionUID = -1845966565955648130L;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open"})
    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "THEME_ID")
    private Theme theme;

    @Composition
    @OneToMany(mappedBy = "themePublication")
    private List<DataProduct> dataProducts;

    @NotNull
    @Column(name = "DATA_CLASS", nullable = false)
    private String dataClass;

    @Column(name = "CLASS_SUFFIX_OVERRIDE", length = 50)
    private String classSuffixOverride;

    @Column(name = "SIMI_CLASS_SUFFIX")
    private String simiClassSuffix;

    @JoinTable(name = "SIMI_THEME_PUBLICATION_CUSTOM_FILE_TYPE_LINK",
            joinColumns = @JoinColumn(name = "THEME_PUBLICATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "CUSTOM_FILE_TYPE_ID"))
    @ManyToMany
    private List<CustomFileType> customFileTypes;

    public List<DataProduct> getDataProducts() {
        return dataProducts;
    }

    public void setDataProducts(List<DataProduct> dataProducts) {
        this.dataProducts = dataProducts;
    }

    public String deferFullIdent(){
        String res = theme.getIdentifier();

        String suffix = deferSuffix();
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
                suffix = null;
                break;
            case NON_TABULAR:
                suffix = classSuffixOverride;
                break;
            default:
                suffix = "error";
                break;
        }

        return suffix;
    }

    public List<CustomFileType> getCustomFileTypes() {
        return customFileTypes;
    }

    public void setCustomFileTypes(List<CustomFileType> customFileTypes) {
        this.customFileTypes = customFileTypes;
    }

    public String getSimiClassSuffix() {
        return simiClassSuffix;
    }

    public void setSimiClassSuffix(String simiClassSuffix) {
        this.simiClassSuffix = simiClassSuffix;
    }

    public String getClassSuffixOverride() {
        return classSuffixOverride;
    }

    public void setClassSuffixOverride(String classSuffixOverride) {
        this.classSuffixOverride = classSuffixOverride;
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