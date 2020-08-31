package ch.so.agi.simi.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "SIMI_DATA_PRODUCT_PUB_SCOPE")
@Entity(name = "simi_DataProduct_PubScope")
@NamePattern("%s|displayText")
public class DataProduct_PubScope extends StandardEntity {
    private static final long serialVersionUID = -1191099388059651336L;

    @NotNull
    @Column(name = "DISPLAY_TEXT", nullable = false, length = 100)
    private String displayText;

    @NotNull
    @Column(name = "OVERALL_STATE", nullable = false)
    private Integer overallState;

    @NotNull
    @Column(name = "DEFAULT_VALUE", nullable = false)
    private Boolean defaultValue = false;

    @NotNull
    @Column(name = "FOR_DSV", nullable = false)
    private Boolean forDSV = false;

    @NotNull
    @Column(name = "FOR_GROUP", nullable = false)
    private Boolean forGroup = false;

    @NotNull
    @Column(name = "FOR_MAP", nullable = false)
    private Boolean forMap = false;

    @NotNull
    @Column(name = "PUB_TO_WMS", nullable = false)
    private Boolean pubToWMS = false;

    @NotNull
    @Column(name = "PUB_TO_WGC", nullable = false)
    private Boolean pubToWGC = false;

    @NotNull
    @Column(name = "PUB_TO_LOCATOR", nullable = false)
    private Boolean pubToLocator = false;

    @NotNull
    @Column(name = "SORT", nullable = false)
    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getPubToLocator() {
        return pubToLocator;
    }

    public void setPubToLocator(Boolean pubToLocator) {
        this.pubToLocator = pubToLocator;
    }

    public Boolean getPubToWGC() {
        return pubToWGC;
    }

    public void setPubToWGC(Boolean pubToWGC) {
        this.pubToWGC = pubToWGC;
    }

    public Boolean getPubToWMS() {
        return pubToWMS;
    }

    public void setPubToWMS(Boolean pubToWMS) {
        this.pubToWMS = pubToWMS;
    }

    public Boolean getForMap() {
        return forMap;
    }

    public void setForMap(Boolean forMap) {
        this.forMap = forMap;
    }

    public Boolean getForGroup() {
        return forGroup;
    }

    public void setForGroup(Boolean forGroup) {
        this.forGroup = forGroup;
    }

    public Boolean getForDSV() {
        return forDSV;
    }

    public void setForDSV(Boolean forDSV) {
        this.forDSV = forDSV;
    }

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public DataProduct_PubScope_OverallStateEnum getOverallState() {
        return overallState == null ? null : DataProduct_PubScope_OverallStateEnum.fromId(overallState);
    }

    public void setOverallState(DataProduct_PubScope_OverallStateEnum overallState) {
        this.overallState = overallState == null ? null : overallState.getId();
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }
}