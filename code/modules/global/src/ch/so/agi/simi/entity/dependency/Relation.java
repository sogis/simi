package ch.so.agi.simi.entity.dependency;

import ch.so.agi.simi.entity.product.DataSetView;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIDEPENDENCY_RELATION", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_SIMI_RELATION_UNQ", columnNames = {"RELATION_TYPE", "DEPENDENCY_ID", "DATA_SET_VIEW_ID"})
})
@Entity(name = "simiDependency_Relation")
public class Relation extends StandardEntity {
    private static final long serialVersionUID = -7067104417294138445L;

    @NotNull
    @Column(name = "RELATION_TYPE", nullable = false)
    private String relationType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DEPENDENCY_ID")
    private Dependency dependency;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DATA_SET_VIEW_ID")
    private DataSetView dataSetView;

    public DataSetView getDataSetView() {
        return dataSetView;
    }

    public void setDataSetView(DataSetView dataSetView) {
        this.dataSetView = dataSetView;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public void setDependency(Dependency dependency) {
        this.dependency = dependency;
    }

    public RelationType getRelationType() {
        return relationType == null ? null : RelationType.fromId(relationType);
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType == null ? null : relationType.getId();
    }
}