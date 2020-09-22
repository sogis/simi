package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.List;

@Table(name = "SIMIPRODUCT_FACADE_LAYER")
@Entity(name = "simiProduct_FacadeLayer")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("%s|identifier")
public class FacadeLayer extends SingleActor {
    private static final long serialVersionUID = -5231187031797128001L;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "facadeLayer")
    @OnDeleteInverse(DeletePolicy.UNLINK)
    private List<PropertiesInFacade> dataSetViews;

    public List<PropertiesInFacade> getDataSetViews() {
        return dataSetViews;
    }

    public void setDataSetViews(List<PropertiesInFacade> dataSetViews) {
        this.dataSetViews = dataSetViews;
    }
}