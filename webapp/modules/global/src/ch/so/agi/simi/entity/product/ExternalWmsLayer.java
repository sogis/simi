package ch.so.agi.simi.entity.product;

import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMIPRODUCT_EXTERNAL_WMS_LAYERS")
@Entity(name = ExternalWmsLayer.NAME)
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
public class ExternalWmsLayer extends SingleActor {
    public static final String NAME = "simiProduct_ExternalWmsLayers";

    private static final long serialVersionUID = 640051427821660156L;

    @NotNull
    @Column(name = "IDENTIFIER_LIST", nullable = false)
    private String externalIdentifier;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SERVICE_ID")
    private ExternalWmsService service;

    @Override
    protected String typeAbbreviation(){
        return "Ext. WMS-Layers";
    }

    public ExternalWmsService getService() {
        return service;
    }

    public void setService(ExternalWmsService service) {
        this.service = service;
    }

    public String getExternalIdentifier() {
        return externalIdentifier;
    }

    public void setExternalIdentifier(String externalIdentifier) {
        this.externalIdentifier = externalIdentifier;
    }
}