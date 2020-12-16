package ch.so.agi.simi.entity.product.non_dsv;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.List;

@Table(name = "SIMIPRODUCT_FACADE_LAYER")
@Entity(name = "simiProduct_FacadeLayer")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("#concatName|identifier,title") //needed to define minimal view
public class FacadeLayer extends SingleActor {
    private static final long serialVersionUID = -5231187031797128001L;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "facadeLayer")
    @OrderBy("sort")
    private List<PropertiesInFacade> dataSetViews;

    @Override
    protected String typeAbbreviation(){
        return "Fassade";
    }

    public List<PropertiesInFacade> getDataSetViews() {
        return dataSetViews;
    }

    public void setDataSetViews(List<PropertiesInFacade> dataSetViews) {
        this.dataSetViews = dataSetViews;
    }
}