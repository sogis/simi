package ch.so.agi.simi.entity.featureinfo;

import ch.so.agi.simi.entity.featureinfo.FeatureInfo;
import ch.so.agi.simi.entity.featureinfo.LayerRelation;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "PRODUCT_DATA_SET_VIEW")
@Entity(name = "product_DataSetView")
@NamePattern("#getT_dispName|identifier")
public class DataSetView extends StandardEntity {
    private static final long serialVersionUID = 3720829701428961919L;

    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "dataSetView")
    private List<LayerRelation> fiLayerRelations;

    @NotNull
    @Column(name = "IDENTIFIER", nullable = false)
    private String identifier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEATURE_INFO_ID")
    private FeatureInfo featureInfo;

    @Transient
    @MetaProperty(mandatory = true)
    @NotNull
    public String getT_dispName() {
        return identifier + " (DSV)";
    }

    public FeatureInfo getFeatureInfo() {
        return featureInfo;
    }

    public void setFeatureInfo(FeatureInfo featureInfo) {
        this.featureInfo = featureInfo;
    }

    public List<LayerRelation> getFiLayerRelations() {
        return fiLayerRelations;
    }

    public void setFiLayerRelations(List<LayerRelation> fiLayerRelations) {
        this.fiLayerRelations = fiLayerRelations;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}