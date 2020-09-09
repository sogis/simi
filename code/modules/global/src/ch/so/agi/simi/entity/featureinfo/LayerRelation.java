package ch.so.agi.simi.entity.featureinfo;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "FI_LAYER_RELATION")
@Entity(name = "fi_LayerRelation")
public class LayerRelation extends StandardEntity {
    private static final long serialVersionUID = -6013250778559070747L;

    @NotNull
    @Column(name = "REL_TYPE", nullable = false)
    private String relType;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DATA_SET_VIEW_ID")
    private DataSetView dataSetView;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FEATURE_INFO_ID")
    private FeatureInfo featureInfo;

    public FeatureInfo getFeatureInfo() {
        return featureInfo;
    }

    public void setFeatureInfo(FeatureInfo featureInfo) {
        this.featureInfo = featureInfo;
    }

    public DataSetView getDataSetView() {
        return dataSetView;
    }

    public void setDataSetView(DataSetView dataSetView) {
        this.dataSetView = dataSetView;
    }

    public RelationType getRelType() {
        return relType == null ? null : RelationType.fromId(relType);
    }

    public void setRelType(RelationType relType) {
        this.relType = relType == null ? null : relType.getId();
    }
}