package ch.so.agi.simi.entity.data.tabular;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Table(name = "SIMI_EXTERNAL_TABLE")
@Entity(name = "simi_ExternalTable")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
public class ExternalTable extends TableDS {
    private static final long serialVersionUID = 318922531144053370L;
}