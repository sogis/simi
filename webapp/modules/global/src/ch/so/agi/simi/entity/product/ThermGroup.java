package ch.so.agi.simi.entity.product;

import ch.so.agi.simi.entity.SimiEntity;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIPRODUCT_THERM_GROUP")
@Entity(name = ThermGroup.NAME)
@NamePattern("%s|name")
public class ThermGroup extends SimiEntity {

    public static final String NAME = "simiProduct_ThermGroup";

    private static final long serialVersionUID = -1765570444728249395L;

    @NotNull
    @Column(name = "NAME", nullable = false, unique = true)
    private String name = "Muster: theme | theme.dataset | theme.datasetview";

    @Column(name = "KEYWORDS", length = 800)
    private String keywords;

    @Column(name = "SYNONYMS", length = 800)
    private String synonyms;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}