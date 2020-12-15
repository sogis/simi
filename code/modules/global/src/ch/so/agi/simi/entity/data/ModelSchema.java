package ch.so.agi.simi.entity.data;

import ch.so.agi.simi.entity.SimiStandardEntity;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIDATA_MODEL_SCHEMA", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_SIMIDATA_MODEL_SCHEMA_UNQ_SCHEMA_NAME_POSTGRES_DB_ID", columnNames = {"SCHEMA_NAME", "POSTGRES_DB_ID"})
})
@Entity(name = "simiData_ModelSchema")
@NamePattern("%s.%s|postgresDB,schemaName")
public class ModelSchema extends SimiStandardEntity {
    private static final long serialVersionUID = -2988394575142052644L;

    @Column(name = "SCHEMA_NAME", nullable = false, length = 100)
    @NotNull
    private String schemaName;

    @Column(name = "MODEL_NAME", length = 100)
    private String modelName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POSTGRES_DB_ID")
    private PostgresDB postgresDB;

    @OneToMany(mappedBy = "modelSchema")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
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