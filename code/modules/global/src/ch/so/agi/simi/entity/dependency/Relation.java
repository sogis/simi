package ch.so.agi.simi.entity.dependency;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.product.datasetview.DataSetView;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIDEPENDENCY_RELATION", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_SIMI_RELATION_UNQ", columnNames = {"RELATION_TYPE", "DEPENDENCY_ID", "DATA_SET_VIEW_ID"})
})
@Entity(name = Relation.NAME)
public class Relation extends SimiEntity {

    public static final String NAME = "simiDependency_Relation";

    private static final long serialVersionUID = -7067104417294138445L;

    @NotNull
    @Column(name = "RELATION_TYPE", nullable = false)
    private String relationType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DEPENDENCY_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Dependency dependency;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DATA_SET_VIEW_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
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

    @PostConstruct
    public void postConstruct() {
        setRelationType(RelationType.DATA);
    }
}