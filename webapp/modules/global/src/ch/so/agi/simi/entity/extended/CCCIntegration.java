package ch.so.agi.simi.entity.extended;

import ch.so.agi.simi.entity.product.Map;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = CCCIntegration.NAME)
public class CCCIntegration extends Dependency {

    public static final String NAME = "simiExtended_CCCIntegration";

    private static final long serialVersionUID = -7415262245786732300L;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @OnDeleteInverse(DeletePolicy.DENY)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAP_ID")
    @NotNull
    private Map map;

    @Lob
    @Column(name = "LOCATOR_LAYERS")
    @NotNull
    private String locatorLayers;

    @Lob
    @Column(name = "NOTIFY_LAYERS")
    @NotNull
    private String notifyLayers;

    @Override
    protected String typeAbbreviation(){
        return "CCC-Integration";
    }

    public String getLocatorLayers() {
        return locatorLayers;
    }

    public void setLocatorLayers(String locatorLayers) {
        this.locatorLayers = locatorLayers;
    }

    public String getNotifyLayers() {
        return notifyLayers;
    }

    public void setNotifyLayers(String notifyLayers) {
        this.notifyLayers = notifyLayers;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}