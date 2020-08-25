package ch.so.agi.simi.entity.iam;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "SIMI_ROLE")
@Entity(name = "simi_Role")
@NamePattern("%s|name")
public class Role extends StandardEntity {
    private static final long serialVersionUID = -1282147615227159232L;

    @NotNull
    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    private String name;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}