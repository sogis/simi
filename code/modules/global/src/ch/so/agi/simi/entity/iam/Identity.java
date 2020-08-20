package ch.so.agi.simi.entity.iam;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "SIMI_IDENTITY")
@Entity(name = "simi_Identity")
@NamePattern("%s|identifier")
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