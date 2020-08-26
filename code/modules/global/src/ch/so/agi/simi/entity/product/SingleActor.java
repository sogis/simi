package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.List;

@Table(name = "SIMI_SINGLE_ACTOR")
@Entity(name = "simi_SingleActor")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
public class SingleActor extends DataProduct {
    private static final long serialVersionUID = 7629032894155576081L;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "singleActor")
    private List<PropertiesInList> productLists;

    public List<PropertiesInList> getProductLists() {
        return productLists;
    }

    public void setProductLists(List<PropertiesInList> productLists) {
        this.productLists = productLists;
    }
}