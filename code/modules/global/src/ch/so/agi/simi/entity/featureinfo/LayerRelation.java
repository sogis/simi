package ch.so.agi.simi.entity.featureinfo;

import ch.so.agi.simi.entity.SimiStandardEntity;
import ch.so.agi.simi.entity.product.DataSetView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIFEATUREINFO_LAYER_RELATION", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_SIMI_LAYER_RELATION_UNQ_FEATURE_INFO_ID_DATA_SET_VIEW_ID", columnNames = {"FEATURE_INFO_ID", "DATA_SET_VIEW_ID"})
})
@Entity(name = "simiFeatureInfo_LayerRelation")
public class LayerRelation extends SimiStandardEntity {
    private static final long serialVersionUID = -6013250778559070747L;

    @NotNull
    @Column(name = "RELATION", nullable = false)
    private Integer relation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FEATURE_INFO_ID")
    @NotNull
    private FeatureInfo featureInfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DATA_SET_VIEW_ID")
    @NotNull
    private DataSetView dataSetView;

    public DataSetView getDataSetView() {
        return dataSetView;
    }

    public void setDataSetView(DataSetView dataSetView) {
        this.dataSetView = dataSetView;
    }

    public FeatureInfo getFeatureInfo() {
        return featureInfo;
    }

    public void setFeatureInfo(FeatureInfo featureInfo) {
        this.featureInfo = featureInfo;
    }

    public LayerRelationEnum getRelation() {
        return relation == null ? null : LayerRelationEnum.fromId(relation);
    }

    public void setRelation(LayerRelationEnum relation) {
        this.relation = relation == null ? null : relation.getId();
    }
}