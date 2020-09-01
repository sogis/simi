package ch.so.agi.simi.entity.data.tabular;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMI_MODEL_SCHEMA")
@Entity(name = "simi_ModelSchema")
@NamePattern("%s.%s|postgresDB,schemaName")
public class ModelSchema extends StandardEntity {
    private static final long serialVersionUID = -2988394575142052644L;

    @Column(name = "SCHEMA_NAME", nullable = false, unique = true, length = 100)
    @NotNull
    private String schemaName;

    @Column(name = "MODEL_NAME", unique = true, length = 100)
    private String modelName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POSTGRES_DB_ID", unique = true)
    private PostgresDB postgresDB;

    @OneToMany(mappedBy = "modelSchema")
    private List<PostgresTable> postgresTables;

    public List<PostgresTable> getPostgresTables() {
        return postgresTables;
    }

    public void setPostgresTables(List<PostgresTable> postgresTables) {
        this.postgresTables = postgresTables;
    }

    public PostgresDB getPostgresDB() {
        return postgresDB;
    }

    public void setPostgresDB(PostgresDB postgresDB) {
        this.postgresDB = postgresDB;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
}