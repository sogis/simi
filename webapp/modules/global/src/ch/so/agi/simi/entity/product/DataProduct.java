package ch.so.agi.simi.entity.product;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.theme.ThemePublication;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIPRODUCT_DATA_PRODUCT")
@Entity(name = DataProduct.NAME)
@NamePattern("#concatName|identifier,title")
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
public class DataProduct extends SimiEntity {

    public static final String NAME = "simiProduct_DataProduct";

    private static final long serialVersionUID = -3456773582487680912L;

    @NotNull
    @Column(name = "IDENTIFIER", nullable = false, unique = true, length = 100)
    private String identifier;

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

    @Column(name = "_KEYWORDS_DEPRECATED", length = 500)
    private String _keywords_deprecated;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "_SYNONYMS_DEPRECATED", length = 800)
    private String _synonyms_deprecated;

    @Column(name = "TITLE", length = 200)
    private String title;

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
        return this.identifier + " | " + this.title + " | " +  typeAbbreviation() ;
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

    public String get_synonyms_deprecated() {
        return _synonyms_deprecated;
    }

    public void set_synonyms_deprecated(String _synonyms_deprecated) {
        this._synonyms_deprecated = _synonyms_deprecated;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String get_keywords_deprecated() {
        return _keywords_deprecated;
    }

    public void set_keywords_deprecated(String _keywords_deprecated) {
        this._keywords_deprecated = _keywords_deprecated;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}