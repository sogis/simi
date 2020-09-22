package ch.so.agi.simi.entity.ccc;

import ch.so.agi.simi.entity.SimiStandardEntity;
import ch.so.agi.simi.entity.product.Map;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMICCC_CCC_CLIENT")
@Entity(name = "simiCCC_CCCClient")
@NamePattern("%s|title")
public class CCCClient extends SimiStandardEntity {
    private static final long serialVersionUID = -2900163207066720488L;

    @NotNull
    @Column(name = "KEY_", nullable = false, length = 100)
    private String key;

    @NotNull
    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @NotNull
    @Column(name = "EDIT_GEOM_TYPE", nullable = false)
    private Integer editGeomType = CCCClientEditGeomTypeEnum.POINT.getId();

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "cccClient")
    private List<LocatorLayer> locatorLayers;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "cccClient")
    private List<NotifyLayer> notifyLayers;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MAP_ID")
    private Map map;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public List<NotifyLayer> getNotifyLayers() {
        return notifyLayers;
    }

    public void setNotifyLayers(List<NotifyLayer> notifyLayers) {
        this.notifyLayers = notifyLayers;
    }

    public List<LocatorLayer> getLocatorLayers() {
        return locatorLayers;
    }

    public void setLocatorLayers(List<LocatorLayer> locatorLayers) {
        this.locatorLayers = locatorLayers;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public CCCClientEditGeomTypeEnum getEditGeomType() {
        return editGeomType == null ? null : CCCClientEditGeomTypeEnum.fromId(editGeomType);
    }

    public void setEditGeomType(CCCClientEditGeomTypeEnum editGeomType) {
        this.editGeomType = editGeomType == null ? null : editGeomType.getId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}