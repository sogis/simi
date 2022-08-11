package ch.so.agi.simi.entity.product;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.theme.ThemePublication;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIPRODUCT_DATA_PRODUCT")
@Entity(name = DataProduct.NAME)
@NamePattern("#concatName|title")
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
public class DataProduct extends SimiEntity {

    public static final String NAME = "simiProduct_DataProduct";

    private static final long serialVersionUID = -3456773582487680912L;

    @Column(name = "TITLE", length = 200)
    private String title;

    @NotNull
    @Column(name = "DERIVED_IDENTIFIER", nullable = false, unique = true, length = 100)
    private String derivedIdentifier;

    @Column(name = "IDENT_PART", length = 100)
    private String identPart;

    @Column(name = "KEYWORDS", length = 800)
    private String keywords;

    @Column(name = "SYNONYMS", length = 800)
    private String synonyms;

    @NotNull
    @Column(name = "THEME_ONLY_FOR_ORG", nullable = false)
    private Boolean themeOnlyForOrg = false;

    @NotNull
    @Column(name = "IDENT_IS_PARTIAL", nullable = false)
    private Boolean identIsPartial = false;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open"})
    @NotNull
    @OnDeleteInverse(DeletePolicy.DENY)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "THEME_PUBLICATION_ID")
    private ThemePublication themePublication;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @JoinColumn(name = "PUB_SCOPE_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private DataProduct_PubScope pubScope;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Boolean getThemeOnlyForOrg() {
        return themeOnlyForOrg;
    }

    public void setThemeOnlyForOrg(Boolean themeOnlyForOrg) {
        this.themeOnlyForOrg = themeOnlyForOrg;
    }

    public Boolean getIdentIsPartial() {
        return identIsPartial;
    }

    public void setIdentIsPartial(Boolean identIsPartial) {
        this.identIsPartial = identIsPartial;
    }

    public String getDerivedIdentifier() {
        return derivedIdentifier;
    }

    public void setDerivedIdentifier(String derivedIdentifier) {
        this.derivedIdentifier = derivedIdentifier;
    }

    public String getIdentPart() {
        return identPart;
    }

    public void setIdentPart(String identPart) {
        this.identPart = identPart;
    }

    public ThemePublication getThemePublication() {
        return themePublication;
    }

    public void setThemePublication(ThemePublication themePublication) {
        this.themePublication = themePublication;
    }

    protected String typeAbbreviation(){
        return " WARNING: override missing.";
    }

    public String concatName(){
        return this.derivedIdentifier + " | " + this.title + " | " +  typeAbbreviation() ;
    }

    @MetaProperty
    public String getEntityName() { // For use in Tables, can be referenced as entityName
        return concatName();
    }

    @MetaProperty
    public String getTypeAbbreviation() { // For use in Tables, can be referenced as typeAbbreviation
        return typeAbbreviation();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataProduct_PubScope getPubScope() {
        return pubScope;
    }

    public void setPubScope(DataProduct_PubScope pubScope) {
        this.pubScope = pubScope;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String deriveIdentifier(ThemePublication tp){
        String res = null;

        if(identIsPartial)
            res = tp.deferFullIdent() + "." + identPart;
        else
            res = identPart;

        return res;
    }

    public void updateDerivedIdentifier(DataManager manager){
        ThemePublication pub = manager.load(ThemePublication.class).
                view("dProd-before-persist").
                id(this.getThemePublication().getId())
                .one();

        String derived = null;

        if(identIsPartial){
            if(identPart != null)
                derived = pub.deferFullIdent() + "." + identPart;
            else
                derived = pub.deferFullIdent();
        }
        else {
            derived = identPart;
        }

        this.setDerivedIdentifier(derived);
    }
}