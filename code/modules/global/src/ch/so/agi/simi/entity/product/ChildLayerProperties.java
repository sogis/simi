package ch.so.agi.simi.entity.product;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public class ChildLayerProperties extends StandardEntity {
    private static final long serialVersionUID = 4170602148331630171L;

    @NotNull
    @Column(name = "SORT", nullable = false)
    private Integer sort;

    @NotNull
    @Column(name = "TRANSPARENCY", nullable = false)
    @Max(100)
    @Min(0)
    private Integer transparency;

    public Integer getTransparency() {
        return transparency;
    }

    public void setTransparency(Integer transparency) {
        this.transparency = transparency;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}