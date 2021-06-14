package ch.so.agi.simi.entity.product;

import ch.so.agi.simi.entity.SimiEntity;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;

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

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @JoinColumn(name = "PUB_SCOPE_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private DataProduct_PubScope pubScope;

    @Column(name = "KEYWORDS", length = 500)
    private String keywords;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "SYNONYMS", length = 800)
    private String synonyms;

    @Column(name = "TITLE", length = 200)
    private String title;

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

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
}