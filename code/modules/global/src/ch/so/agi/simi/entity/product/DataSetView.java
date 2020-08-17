package ch.so.agi.simi.entity.product;

import ch.so.agi.simi.entity.featureinfo.LayerRelation;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "PRODUCT_DATA_SET_VIEW")
@Entity(name = "product_DataSetView")
public class DataSetView extends StandardEntity {
    private static final long serialVersionUID = 3720829701428961919L;

    @NotNull
    @Column(name = "IDENTIFIER", nullable = false)
    private String identifier;

    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "dataSetView")
    private List<LayerRelation> fiLayerRelations;

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