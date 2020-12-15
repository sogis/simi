package ch.so.agi.simi.entity.product.non_dsv;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIPRODUCT_SINGLE_ACTOR")
@Entity(name = "simiProduct_SingleActor")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("entityName|identifier,title") //needed to define minimal view
public class SingleActor extends DataProduct {
    private static final long serialVersionUID = 7629032894155576081L;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
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