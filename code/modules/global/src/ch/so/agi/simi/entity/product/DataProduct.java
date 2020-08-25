package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table(name = "SIMI_DATA_PRODUCT")
@Entity(name = "simi_DataProduct")
@NamePattern("%s|title")
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
public class DataProduct extends StandardEntity {
    private static final long serialVersionUID = -3456773582487680912L;

    @NotNull
    @Column(name = "IDENTIFIER", nullable = false, unique = true, length = 100)
    private String identifier;

    @Column(name = "KEYWORDS", length = 200)
    private String keywords;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "SYNONYMS", length = 200)
    private String synonyms;

    @Column(name = "TITLE", length = 200)
    private String title;

    @Column(name = "RELEASED_AT")
    private LocalDateTime releasedAt;

    @Column(name = "RELEASED_THROUGH", length = 100)
    private String releasedThrough;

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