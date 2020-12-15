package ch.so.agi.simi.entity.product.non_dsv;

import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Table(name = "SIMIPRODUCT_LAYER_GROUP")
@Entity(name = "simiProduct_LayerGroup")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("entityName|identifier,title") //needed to define minimal view
public class LayerGroup extends ProductList {
    private static final long serialVersionUID = 5806919831602321399L;

    @Override
    protected String typeAbbreviation(){
        return "Gruppe";
    }
}