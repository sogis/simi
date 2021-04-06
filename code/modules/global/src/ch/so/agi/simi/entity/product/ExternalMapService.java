package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "SIMI_EXTERNAL_MAP_SERVICE")
@Entity(name = ExternalMapService.NAME)
@NamePattern("#concatName|url, serviceType")
public class ExternalMapService extends StandardEntity {

    public static final String NAME = "simiProduct_ExternalMapService";

    private static final long serialVersionUID = 6461435790259829056L;

    @NotNull
    @Column(name = "SERVICE_TYPE", nullable = false)
    private String serviceType;

    @NotNull
    @Column(name = "URL", nullable = false, unique = true)
    private String url;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    public void setServiceType(ExternalMapService_Type serviceType) {
        this.serviceType = serviceType == null ? null : serviceType.getId();
    }

    public ExternalMapService_Type getServiceType() {
        return serviceType == null ? null : ExternalMapService_Type.fromId(serviceType);
    }

    public String concatName(){
        return url + " (" + serviceType +  ")";
    }

    @MetaProperty
    public String getServiceTypeString() {
        if(serviceType == null)
            return "";

        return serviceType;
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