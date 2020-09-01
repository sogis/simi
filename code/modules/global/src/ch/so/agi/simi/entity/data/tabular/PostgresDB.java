package ch.so.agi.simi.entity.data.tabular;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMI_POSTGRES_DB")
@Entity(name = "simi_PostgresDB")
@NamePattern("%s|dbName")
public class PostgresDB extends StandardEntity {
    private static final long serialVersionUID = 5599910294180509457L;

    @Column(name = "DB_NAME", nullable = false, unique = true, length = 100)
    @NotNull
    private String dbName;

    @Column(name = "DEFAULT_VALUE")
    private Boolean defaultValue;

    @OneToMany(mappedBy = "postgresDB")
    private List<ModelSchema> modelSchemas;

    public List<ModelSchema> getModelSchemas() {
        return modelSchemas;
    }

    public void setModelSchemas(List<ModelSchema> modelSchemas) {
        this.modelSchemas = modelSchemas;
    }

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}