package ch.so.agi.simi.entity.data.tabular;

import ch.so.agi.simi.entity.SimiStandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIDATA_POSTGRES_DB")
@Entity(name = "simiData_PostgresDB")
@NamePattern("%s|dbName")
public class PostgresDB extends SimiStandardEntity {
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