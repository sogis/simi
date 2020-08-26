package ch.so.agi.simi.entity.product;

import javax.persistence.*;

@Table(name = "SIMI_PROPERTIES_IN_LIST")
@Entity(name = "simi_PropertiesInList")
public class PropertiesInList extends ChildLayerProperties {
    private static final long serialVersionUID = 3933249168854903665L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_LIST_ID")
    private ProductList productList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SINGLE_ACTOR_ID")
    private SingleActor singleActor;

    public SingleActor getSingleActor() {
        return singleActor;
    }

    public void setSingleActor(SingleActor singleActor) {
        this.singleActor = singleActor;
    }

    public ProductList getProductList() {
        return productList;
    }

    public void setProductList(ProductList productList) {
        this.productList = productList;
    }
}