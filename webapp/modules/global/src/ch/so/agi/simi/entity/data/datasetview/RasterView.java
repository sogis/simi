package ch.so.agi.simi.entity.data.datasetview;

import ch.so.agi.simi.entity.data.RasterDS;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIDATA_RASTER_VIEW")
@Entity(name = RasterView.NAME)
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("#concatName|derivedIdentifier,title") //needed to define minimal view
public class RasterView extends DataSetView {

    public static final String NAME = "simiData_RasterView";

    private static final long serialVersionUID = 1986595030057944078L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RASTER_DS_ID")
    @NotNull
    @OnDeleteInverse(DeletePolicy.DENY)
    private RasterDS rasterDS;

    @Column(name = "KEYWORDS_ARR", length = 800)
    private String keywordsArr;

    @Column(name = "SYNONYMS_ARR", length = 800)
    private String synonymsArr;

    public String getSynonymsArr() {
        return synonymsArr;
    }

    public void setSynonymsArr(String synonymsArr) {
        this.synonymsArr = synonymsArr;
    }

    public String getKeywordsArr() {
        return keywordsArr;
    }

    public void setKeywordsArr(String keywordsArr) {
        this.keywordsArr = keywordsArr;
    }

    public RasterDS getRasterDS() {
        return rasterDS;
    }

    public void setRasterDS(RasterDS rasterDS) {
        this.rasterDS = rasterDS;
    }
}