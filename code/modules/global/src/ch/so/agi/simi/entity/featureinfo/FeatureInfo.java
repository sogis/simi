package ch.so.agi.simi.entity.featureinfo;

import ch.so.agi.simi.entity.SimiStandardEntity;
import ch.so.agi.simi.entity.product.DataSetView;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "SIMIFEATUREINFO_FEATURE_INFO")
@Entity(name = "simiFeatureInfo_FeatureInfo")
public class FeatureInfo extends SimiStandardEntity {
    private static final long serialVersionUID = 3002954753875689457L;

    @NotNull
    @Lob
    @Column(name = "DISPLAY_TEMPLATE", nullable = false)
    private String displayTemplate;

    @Lob
    @Column(name = "SQL_QUERY")
    private String sqlQuery;

    @Column(name = "PY_MODULE_NAME", length = 100)
    private String pyModuleName;

    @Lob
    @Column(name = "REMARKS")
    private String remarks;

    @JoinTable(name = "SIMI_FEATURE_INFO_DATA_SET_VIEW_LINK",
            joinColumns = @JoinColumn(name = "FEATURE_INFO_ID"),
            inverseJoinColumns = @JoinColumn(name = "DATA_SET_VIEW_ID"))
    @OnDelete(DeletePolicy.CASCADE)
    @ManyToMany
    private List<DataSetView> queryDataSetViews;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DATASETVIEW_ID", unique = true)
    private DataSetView dataSetView;

    public DataSetView getDataSetView() {
        return dataSetView;
    }

    public void setDataSetView(DataSetView dataSetView) {
        this.dataSetView = dataSetView;
    }

    public List<DataSetView> getQueryDataSetViews() {
        return queryDataSetViews;
    }

    public void setQueryDataSetViews(List<DataSetView> queryDataSetViews) {
        this.queryDataSetViews = queryDataSetViews;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPyModuleName() {
        return pyModuleName;
    }

    public void setPyModuleName(String pyModuleName) {
        this.pyModuleName = pyModuleName;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getDisplayTemplate() {
        return displayTemplate;
    }

    public void setDisplayTemplate(String displayTemplate) {
        this.displayTemplate = displayTemplate;
    }
}