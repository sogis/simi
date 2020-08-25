package ch.so.agi.simi.entity.product;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Table(name = "SIMI_PRODUCT_LIST")
@Entity(name = "simi_ProductList")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
public class ProductList extends DataProduct {
    private static final long serialVersionUID = 7189309667378680986L;
}