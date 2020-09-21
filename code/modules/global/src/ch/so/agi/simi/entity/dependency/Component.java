package ch.so.agi.simi.entity.dependency;

import ch.so.agi.simi.entity.SimiStandardEntity;
import ch.so.agi.simi.entity.product.DataProduct;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIDEPENDENCY_COMPONENT")
@Entity(name = "simiDependency_Component")
@NamePattern("%s|name")
public class Component extends SimiStandardEntity {
    private static final long serialVersionUID = -4862112351147530759L;

    @NotNull
    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    private String name;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    @JoinTable(name = "SIMI_COMPONENT_DATA_PRODUCT_LINK",
            joinColumns = @JoinColumn(name = "COMPONENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "DATA_PRODUCT_ID"))
    private @ManyToMany
    List<DataProduct> dataProducts;

    public void setDataProducts(List<DataProduct> dataProducts) {
        this.dataProducts = dataProducts;
    }

    public List<DataProduct> getDataProducts() {
        return dataProducts;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}