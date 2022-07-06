package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIPRODUCT_SINGLE_ACTOR")
@Entity(name = SingleActor.NAME)
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("#concatName|derivedIdentifier,title") //needed to define minimal view
public class SingleActor extends DataProduct {

    public static final String NAME = "simiProduct_SingleActor";

    private static final long serialVersionUID = 7629032894155576081L;

    @Composition
    @OneToMany(mappedBy = "singleActor")
    @OrderBy("sort")
    private List<PropertiesInList> productLists;

    @NotNull
    @Column(name = "TRANSPARENCY", nullable = false)
    @Min(0)
    @Max(100)
    private Integer transparency = 0;

    @Column(name = "CUSTOM_LEGEND")
    private byte[] customLegend;

    @Column(name = "CUSTOM_LEGEND_SUFFIX", length = 100)
    private String customLegendSuffix;

    public String getCustomLegendSuffix() {
        return customLegendSuffix;
    }

    public void setCustomLegendSuffix(String customLegendSuffix) {
        this.customLegendSuffix = customLegendSuffix;
    }

    public byte[] getCustomLegend() {
        return customLegend;
    }

    public void setCustomLegend(byte[] customLegend) {
        this.customLegend = customLegend;
    }

    public Integer getTransparency() {
        return transparency;
    }

    public void setTransparency(Integer transparency) {
        this.transparency = transparency;
    }

    public List<PropertiesInList> getProductLists() {
        return productLists;
    }

    public void setProductLists(List<PropertiesInList> productLists) {
        this.productLists = productLists;
    }
}