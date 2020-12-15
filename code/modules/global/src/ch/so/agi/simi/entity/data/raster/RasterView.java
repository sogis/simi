package ch.so.agi.simi.entity.data.raster;

import ch.so.agi.simi.entity.product.DataSetView;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIDATA_RASTER_VIEW")
@Entity(name = "simiData_RasterView")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("entityName|identifier,title") //needed to define minimal view
public class RasterView extends DataSetView {
    private static final long serialVersionUID = 1986595030057944078L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RASTER_DS_ID")
    @NotNull
    private RasterDS rasterDS;

    public RasterDS getRasterDS() {
        return rasterDS;
    }

    public void setRasterDS(RasterDS rasterDS) {
        this.rasterDS = rasterDS;
    }
}