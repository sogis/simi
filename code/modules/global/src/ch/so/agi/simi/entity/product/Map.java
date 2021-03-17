package ch.so.agi.simi.entity.product;

import ch.so.agi.simi.entity.extended.CCCIntegration;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIPRODUCT_MAP")
@Entity(name = Map.NAME)
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("#concatName|identifier,title") //needed to define minimal view
public class Map extends ProductList {

    public static final String NAME = "simiProduct_Map";
    private static final long serialVersionUID = -737058404500308993L;

    @NotNull
    @Column(name = "BACKGROUND", nullable = false)
    private Boolean background = false;

    @NotNull
    @Column(name = "WGC_URL_VALUE", nullable = false, length = 50)
    private String wgcUrlValue;
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.DENY)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "map")
    private CCCIntegration cccIntegration;

    public CCCIntegration getCccIntegration() {
        return cccIntegration;
    }

    public void setCccIntegration(CCCIntegration cccIntegration) {
        this.cccIntegration = cccIntegration;
    }

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