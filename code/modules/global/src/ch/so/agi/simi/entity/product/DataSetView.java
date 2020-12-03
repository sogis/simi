package ch.so.agi.simi.entity.product;

import ch.so.agi.simi.entity.ccc.LocatorLayer;
import ch.so.agi.simi.entity.ccc.NotifyLayer;
import ch.so.agi.simi.entity.featureinfo.FeatureInfo;
import ch.so.agi.simi.entity.iam.Permission;
import ch.so.agi.simi.entity.product.util.StyleAsset;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "SIMIPRODUCT_DATA_SET_VIEW")
@Entity(name = "simiProduct_DataSetView")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("#entityName|identifier,title") //needed to define minimal view
public class DataSetView extends SingleActor {
    private static final long serialVersionUID = 3720829701428961919L;

    @NotNull
    @Column(name = "RAW_DOWNLOAD", nullable = false)
    private Boolean rawDownload = true;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "datasetSetView")
    private List<StyleAsset> styleAssets;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "dataSetView")
    private List<LocatorLayer> locatorLayers;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "dataSetView")
    private List<NotifyLayer> notifyLayers;

    @Lob
    @Column(name = "STYLE_SERVER")
    private String styleServer;

    @Column(name = "STYLE_SERVER_UPLOADED")
    private LocalDateTime styleServerChanged;

    @Lob
    @Column(name = "STYLE_DESKTOP")
    private String styleDesktop;

    @Column(name = "STYLE_DESKTOP_UPLOADED")
    private LocalDateTime styleDesktopChanged;

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
    @OrderBy("sort")
    private List<PropertiesInFacade> facadeLayers;

    @OneToMany(mappedBy = "dataSetView")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<Permission> permissions;

    @JoinTable(name = "SIMI_FEATURE_INFO_DATA_SET_VIEW_LINK",
            joinColumns = @JoinColumn(name = "DATA_SET_VIEW_ID"),
            inverseJoinColumns = @JoinColumn(name = "FEATURE_INFO_ID"))
    @ManyToMany
    private List<FeatureInfo> queryFeatureInfoes;

    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "dataSetView")
    private FeatureInfo featureInfo;

    @Override
    protected String typeAbbreviation(){
        return "View";
    }

    public List<StyleAsset> getStyleAssets() {
        return styleAssets;
    }

    public void setStyleAssets(List<StyleAsset> styleAssets) {
        this.styleAssets = styleAssets;
    }

    public LocalDateTime getStyleServerChanged() {
        return styleServerChanged;
    }

    public void setStyleServerChanged(LocalDateTime styleServerChanged) {
        this.styleServerChanged = styleServerChanged;
    }

    public LocalDateTime getStyleDesktopChanged() {
        return styleDesktopChanged;
    }

    public void setStyleDesktopChanged(LocalDateTime styleDesktopChanged) {
        this.styleDesktopChanged = styleDesktopChanged;
    }

    public FeatureInfo getFeatureInfo() {
        return featureInfo;
    }

    public void setFeatureInfo(FeatureInfo featureInfo) {
        this.featureInfo = featureInfo;
    }

    public List<FeatureInfo> getQueryFeatureInfoes() {
        return queryFeatureInfoes;
    }

    public void setQueryFeatureInfoes(List<FeatureInfo> queryFeatureInfoes) {
        this.queryFeatureInfoes = queryFeatureInfoes;
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

    public Boolean getRawDownload() {
        return rawDownload;
    }

    public void setRawDownload(Boolean rawDownload) {
        this.rawDownload = rawDownload;
    }
}