package ch.so.agi.simi.entity.data;

import ch.so.agi.simi.entity.SimiStandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIDATA_TABLE_DS")
@Entity(name = "simiData_TableDS")
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
@NamePattern("%s|tableName")
public class TableDS extends SimiStandardEntity {
    private static final long serialVersionUID = -2643498680251164635L;

    @Column(name = "TABLE_NAME", nullable = false, length = 100)
    @NotNull
    private String tableName;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}