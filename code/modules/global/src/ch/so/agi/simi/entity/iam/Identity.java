package ch.so.agi.simi.entity.iam;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIIAM_IDENTITY")
@Entity(name = "simiIAM_Identity")
@NamePattern("%s|identifier")
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
public class Identity extends StandardEntity {
    private static final long serialVersionUID = 8795833236734020501L;

    @Column(name = "IDENTIFIER", nullable = false, unique = true, length = 100)
    @NotNull
    private String identifier;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}