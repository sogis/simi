package ch.so.agi.simi.entity.product;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Table(name = "SIMIPRODUCT_LAYER_GROUP")
@Entity(name = "simiProduct_LayerGroup")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
public class LayerGroup extends ProductList {
    private static final long serialVersionUID = 5806919831602321399L;
}