package ch.so.agi.simi.entity.product.non_dsv;

import ch.so.agi.simi.entity.SimiStandardEntity;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "SIMIPRODUCT_DATA_PRODUCT")
@Entity(name = DataProduct.NAME)
@NamePattern("#concatName|identifier,title")
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
public class DataProduct extends SimiStandardEntity {

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

    @Column(name = "SYNONYMS", length = 500)
    private String synonyms;

    @Column(name = "TITLE", length = 200)
    private String title;

    @Column(name = "RELEASED_AT")
    private LocalDateTime releasedAt;

    @Column(name = "RELEASED_THROUGH", length = 100)
    private String releasedThrough;

    protected String typeAbbreviation(){
        return " WARNING: override missing.";
    }

    public String concatName(){
        return this.identifier + " | " + this.title + " | " +  typeAbbreviation() ;
    }

    @Transient
    @MetaProperty
    public String getEntityName() { // For use in Tables, can be referenced as entityName
        return concatName();
    }

    @Transient
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

    public String getReleasedThrough() {
        return releasedThrough;
    }

    public void setReleasedThrough(String releasedThrough) {
        this.releasedThrough = releasedThrough;
    }

    public LocalDateTime getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(LocalDateTime releasedAt) {
        this.releasedAt = releasedAt;
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