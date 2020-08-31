package ch.so.agi.simi.entity.data.tabular;

import ch.so.agi.simi.entity.data.DataSet;

import javax.persistence.*;

@Table(name = "SIMI_TABLE_DS")
@Entity(name = "simi_TableDS")
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
public class TableDS extends DataSet {
    private static final long serialVersionUID = -2643498680251164635L;

    @Column(name = "TABLE_NAME", length = 100)
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}