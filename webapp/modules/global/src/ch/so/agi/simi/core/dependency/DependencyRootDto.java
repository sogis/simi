package ch.so.agi.simi.core.dependency;

import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Entity(name = "simi_DependencyRootDto")
public class DependencyRootDto extends BaseUuidEntity {
    private static final long serialVersionUID = 1379826544680704279L;

    @NotNull
    @Lob
    @Column(name = "DISPLAY", nullable = false)
    private String display;

    @NotNull
    @Column(name = "IS_SCHEMA", nullable = false)
    private Boolean isSchema = false;

    public Boolean getIsSchema() {
        return isSchema;
    }

    public void setIsSchema(Boolean isSchema) {
        this.isSchema = isSchema;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}