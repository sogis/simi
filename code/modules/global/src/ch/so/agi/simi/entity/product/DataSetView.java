package ch.so.agi.simi.entity.product;

import ch.so.agi.simi.entity.ccc.LocatorLayer;
import ch.so.agi.simi.entity.ccc.NotifyLayer;
import ch.so.agi.simi.entity.featureinfo.LayerRelation;
import ch.so.agi.simi.entity.iam.Permission;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIPRODUCT_DATA_SET_VIEW")
@Entity(name = "simiProduct_DataSetView")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("%s|name")
public class DataSetView extends SingleActor {
    private static final long serialVersionUID = 3720829701428961919L;

    @NotNull
    @Column(name = "RAW_DOWNLOAD", nullable = false)
    private Boolean rawDownload = true;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "dataSetView")
    private List<LayerRelation> layerRelations;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "dataSetView")
    private List<LocatorLayer> locatorLayers;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "dataSetView")
    private List<NotifyLayer> notifyLayers;

    @Column(name = "NAME", length = 100)
    private String name;

    @Lob
    @Column(name = "STYLE_SERVER")
    private String styleServer;

    @Lob
    @Column(name = "STYLE_DESKTOP")
    private String styleDesktop;

    @NotNull
    @Column(name = "SEARCH_TYPE", nullable = false)
    private Integer searchType = DataSetView_SearchTypeEnum.NEIN.getId();

    @Column(name = "SEARCH_FACET", length = 100)
    private String searchFacet;

    @Column(name = "SEARCH_FILTER_WORD", length = 100)
    private String searchFilterWord;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "dataSetView")
    private List<PropertiesInFacade> facadeLayers;

    @OneToMany(mappedBy = "dataSetView")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<Permission> permissions;

    public List<LayerRelation> getLayerRelations() {
        return layerRelations;
    }

    public void setLayerRelations(List<LayerRelation> layerRelations) {
        this.layerRelations = layerRelations;
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

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Transient
    @MetaProperty(related = "styleServer")
    public Boolean getStyleServerFilled() {
        return styleServer != null && !styleServer.isEmpty();
    }

    @Transient
    @MetaProperty(related = "styleDesktop")
    public Boolean getStyleDesktopFilled() {
        return styleDesktop != null && !styleDesktop.isEmpty();
    }

    public List<PropertiesInFacade> getFacadeLayers() {
        return facadeLayers;
    }

    public void setFacadeLayers(List<PropertiesInFacade> facadeLayers) {
        this.facadeLayers = facadeLayers;
    }

    public String getSearchFilterWord() {
        return searchFilterWord;
    }

    public void setSearchFilterWord(String searchFilterWord) {
        this.searchFilterWord = searchFilterWord;
    }

    public String getSearchFacet() {
        return searchFacet;
    }

    public void setSearchFacet(String searchFacet) {
        this.searchFacet = searchFacet;
    }

    public DataSetView_SearchTypeEnum getSearchType() {
        return searchType == null ? null : DataSetView_SearchTypeEnum.fromId(searchType);
    }

    public void setSearchType(DataSetView_SearchTypeEnum searchType) {
        this.searchType = searchType == null ? null : searchType.getId();
    }

    public String getStyleDesktop() {
        return styleDesktop;
    }

    public void setStyleDesktop(String styleDesktop) {
        this.styleDesktop = styleDesktop;
    }

    public String getStyleServer() {
        return styleServer;
    }

    public void setStyleServer(String styleServer) {
        this.styleServer = styleServer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRawDownload() {
        return rawDownload;
    }

    public void setRawDownload(Boolean rawDownload) {
        this.rawDownload = rawDownload;
    }
}