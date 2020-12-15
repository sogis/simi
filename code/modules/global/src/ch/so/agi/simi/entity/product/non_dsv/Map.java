package ch.so.agi.simi.entity.product.non_dsv;

import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIPRODUCT_MAP")
@Entity(name = "simiProduct_Map")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("entityName|identifier,title") //needed to define minimal view
public class Map extends ProductList {
    private static final long serialVersionUID = -737058404500308993L;

    @NotNull
    @Column(name = "BACKGROUND", nullable = false)
    private Boolean background = false;

    @NotNull
    @Column(name = "WGC_URL_VALUE", nullable = false, length = 50)
    private String wgcUrlValue;

    @Override
    protected String typeAbbreviation(){
        return "Karte";
    }

    public String getWgcUrlValue() {
        return wgcUrlValue;
    }

    public void setWgcUrlValue(String wgcUrlValue) {
        this.wgcUrlValue = wgcUrlValue;
    }

    public Boolean getBackground() {
        return background;
    }

    public void setBackground(Boolean background) {
        this.background = background;
    }
}