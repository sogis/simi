package ch.so.agi.simi.entity.data;

import ch.so.agi.simi.entity.SimiEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIDATA_POSTGRES_DB")
@Entity(name = PostgresDB.NAME)
public class PostgresDB extends SimiEntity {
    private static final long serialVersionUID = 5599910294180509457L;

    public static final String NAME = "simiData_PostgresDB";

    @NotNull
    @Column(name = "IDENTIFIER", nullable = false, unique = true, length = 100)
    private String identifier;

    @NotNull
    @Column(name = "TITLE", nullable = false, unique = true, length = 100)
    private String title;

    @NotNull
    @Column(name = "DB_SERVICE_URL", nullable = false, unique = true)
    private String dbServiceUrl;

    @Column(name = "DEFAULT_VALUE", nullable = false)
    @NotNull
    private Boolean defaultValue = false;

    @OneToMany(mappedBy = "postgresDB")
    private List<DataTheme> dataThemes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDbServiceUrl() {
        return dbServiceUrl;
    }

    public void setDbServiceUrl(String dbServiceUrl) {
        this.dbServiceUrl = dbServiceUrl;
    }

    public List<DataTheme> getDataThemes() {
        return dataThemes;
    }

    public void setDataThemes(List<DataTheme> dataThemes) {
        this.dataThemes = dataThemes;
    }

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

}