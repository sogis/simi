package ch.so.agi.simi.entity.data;

import ch.so.agi.simi.entity.SimiStandardEntity;
import ch.so.agi.simi.entity.product.datasetview.RasterView;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIDATA_RASTER_DS")
@Entity(name = RasterDS.NAME)
@NamePattern("%s [Raster]|path")
public class RasterDS extends SimiStandardEntity {

    public static final String NAME = "simiData_RasterDS";
    private static final long serialVersionUID = -1371985729459673568L;

    @NotNull
    @Column(name = "PATH", nullable = false, length = 200)
    private String path;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "rasterDS")
    private List<RasterView> rasterViews;

    public List<RasterView> getRasterViews() {
        return rasterViews;
    }

    public void setRasterViews(List<RasterView> rasterViews) {
        this.rasterViews = rasterViews;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}