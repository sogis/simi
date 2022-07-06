package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIPRODUCT_MAP")
@Entity(name = Map.NAME)
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("#concatName|derivedIdentifier,title") //needed to define minimal view
public class Map extends ProductList {

    public static final String NAME = "simiProduct_Map";
    private static final long serialVersionUID = -737058404500308993L;

    @NotNull
    @Column(name = "BACKGROUND", nullable = false)
    private Boolean background = false;

    @Override
    protected String typeAbbreviation(){
        return "Karte";
    }

    public Boolean getBackground() {
        return background;
    }

    public void setBackground(Boolean background) {
        this.background = background;
    }
}