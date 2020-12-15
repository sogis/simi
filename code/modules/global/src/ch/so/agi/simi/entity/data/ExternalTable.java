package ch.so.agi.simi.entity.data;

import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Table(name = "SIMIDATA_EXTERNAL_TABLE")
@Entity(name = "simiData_ExternalTable")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("%s|tableName")
public class ExternalTable extends TableDS {
    private static final long serialVersionUID = 318922531144053370L;
}