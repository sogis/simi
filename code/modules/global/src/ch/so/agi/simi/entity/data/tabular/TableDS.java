package ch.so.agi.simi.entity.data.tabular;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SIMI_TABLE_DS")
@Entity(name = "simi_TableDS")
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
@NamePattern("%s|tableName")
public class TableDS extends StandardEntity {
    private static final long serialVersionUID = -2643498680251164635L;

    @Column(name = "TABLE_NAME", length = 100)
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