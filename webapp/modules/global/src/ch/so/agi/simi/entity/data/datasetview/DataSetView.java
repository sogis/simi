package ch.so.agi.simi.entity.data.datasetview;

import ch.so.agi.simi.entity.extended.Relation;
import ch.so.agi.simi.entity.iam.Permission;
import ch.so.agi.simi.entity.product.PropertiesInFacade;
import ch.so.agi.simi.entity.product.SingleActor;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "SIMIDATA_DATA_SET_VIEW")
@Entity(name = DataSetView.NAME)
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@NamePattern("#concatName|derivedIdentifier,title") //needed to define minimal view
public class DataSetView extends SingleActor {

    public static final String NAME = "simiData_DataSetView";

    private static final long serialVersionUID = 3720829701428961919L;

    @NotNull
    @Column(name = "SERVICE_DOWNLOAD", nullable = false)
    private Boolean serviceDownload = false;

    @NotNull
    @Column(name = "IS_FILE_DOWNLOAD_DSV", nullable = false)
    private Boolean isFileDownloadDSV = false;

    @Composition
    @OneToMany(mappedBy = "dataSetView")
    private List<Relation> relations;

    @Composition
    @OneToMany(mappedBy = "datasetSetView")
    private List<StyleAsset> styleAssets;

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

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "dataSetView")
    @OrderBy("sort")
    private List<PropertiesInFacade> facadeLayers;

    @OneToMany(mappedBy = "dataSetView")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    private List<Permission> permissions;

    public Boolean getIsFileDownloadDSV() {
        return isFileDownloadDSV;
    }

    public void setIsFileDownloadDSV(Boolean isFileDownloadDSV) {
        this.isFileDownloadDSV = isFileDownloadDSV;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
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

    public Boolean getServiceDownload() {
        return serviceDownload;
    }

    public void setServiceDownload(Boolean serviceDownload) {
        this.serviceDownload = serviceDownload;
    }
}