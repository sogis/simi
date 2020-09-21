package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.List;

@Table(name = "SIMIPRODUCT_PRODUCT_LIST")
@Entity(name = "simiProduct_ProductList")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("%s|identifier")
public class ProductList extends DataProduct {
    private static final long serialVersionUID = 7189309667378680986L;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "productList")
    private List<PropertiesInList> singleActors;

    public List<PropertiesInList> getSingleActors() {
        return singleActors;
    }

    public void setSingleActors(List<PropertiesInList> singleActors) {
        this.singleActors = singleActors;
    }
}