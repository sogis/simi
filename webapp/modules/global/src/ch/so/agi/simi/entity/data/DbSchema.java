package ch.so.agi.simi.entity.data;

import ch.so.agi.simi.entity.SimiEntity;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIDATA_DB_SCHEMA", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_SIMIDATA_DB_SCHEMA_UNQ", columnNames = {"SCHEMA_NAME", "POSTGRES_DB_ID"})
})
@Entity(name = DbSchema.NAME)
@NamePattern("#concatName|postgresDB,schemaName")
public class DbSchema extends SimiEntity {
    public static final String NAME = "simiData_DataTheme";

    private static final long serialVersionUID = 6262709342238523498L;

    @Column(name = "SCHEMA_NAME", nullable = false, length = 100)
    @NotNull
    private String schemaName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POSTGRES_DB_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private PostgresDB postgresDB;

    @OneToMany(mappedBy = "dataTheme")
    @Composition
    private List<PostgresTable> postgresTables;

    public String concatName(){
        return this.getPostgresDB().getTitle() + " | " + this.getSchemaName() ;
    }

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

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public PostgresTable findTableByName(String name){
        if(name == null)
            throw new IllegalArgumentException("name must not be null");

        if(postgresTables == null)
            return null;

        PostgresTable res = null;

        for(PostgresTable table : postgresTables){
            if(name.equalsIgnoreCase(table.getTableName())){
                res = table;
                break;
            }
        }

        return res;
    }
}