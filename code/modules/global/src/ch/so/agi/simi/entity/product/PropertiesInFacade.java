package ch.so.agi.simi.entity.product;

import ch.so.agi.simi.entity.data.datasetview.DataSetView;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIPRODUCT_PROPERTIES_IN_FACADE", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_SIMIPRODUCT_PROPERTIES_IN_FACADE_UNQ_DATA_SET_VIEW_ID_FACADE_LAYER_ID", columnNames = {"DATA_SET_VIEW_ID", "FACADE_LAYER_ID"})
})
@Entity(name = "simiProduct_PropertiesInFacade")
public class PropertiesInFacade extends ChildLayerProperties {
    private static final long serialVersionUID = -5482773004746430054L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DATA_SET_VIEW_ID")
    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private DataSetView dataSetView;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FACADE_LAYER_ID")
    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private FacadeLayer facadeLayer;

    public FacadeLayer getFacadeLayer() {
        return facadeLayer;
    }

    public void setFacadeLayer(FacadeLayer facadeLayer) {
        this.facadeLayer = facadeLayer;
    }

    public DataSetView getDataSetView() {
        return dataSetView;
    }

    public void setDataSetView(DataSetView dataSetView) {
        this.dataSetView = dataSetView;
    }
}