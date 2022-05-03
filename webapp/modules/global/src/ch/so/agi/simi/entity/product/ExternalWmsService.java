package ch.so.agi.simi.entity.product;

import ch.so.agi.simi.entity.SimiEntity;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIPRODUCT_EXTERNAL_WMS_SERVICE")
@Entity(name = ExternalWmsService.NAME)
@NamePattern("#concatName|url")
public class ExternalWmsService extends SimiEntity {

    public static final String NAME = "simiProduct_ExternalWmsService";

    private static final long serialVersionUID = 6461435790259829056L;

    @NotNull
    @Column(name = "URL", nullable = false, unique = true)
    private String url;

    @NotNull
    @Column(name = "FEATURE_INFO_FORMAT", nullable = false)
    private String featureInfoFormat;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    public ExternalWmsService_FiFormat getFeatureInfoFormat() {
        return featureInfoFormat == null ? null : ExternalWmsService_FiFormat.fromId(featureInfoFormat);
    }

    public void setFeatureInfoFormat(ExternalWmsService_FiFormat featureInfoFormat) {
        this.featureInfoFormat = featureInfoFormat == null ? null : featureInfoFormat.getId();
    }

    public String concatName(){
        return url;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}