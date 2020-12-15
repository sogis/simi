package ch.so.agi.simi.entity.ccc;

import ch.so.agi.simi.entity.SimiStandardEntity;
import ch.so.agi.simi.entity.product.datasetview.DataSetView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMICCC_LOCATOR_LAYER", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_SIMI_LOCATOR_LAYER_UNQ_CCC_CLIENT_ID_DATA_SET_VIEW_ID_FILTER", columnNames = {"CCC_CLIENT_ID", "DATA_SET_VIEW_ID", "FILTER_"})
})
@Entity(name = "simiCCC_LocatorLayer")
public class LocatorLayer extends SimiStandardEntity {
    private static final long serialVersionUID = -4467081337558731618L;

    @NotNull
    @Column(name = "SORT_INDEX", nullable = false)
    private Integer sort_index;

    @NotNull
    @Column(name = "FILTER_", nullable = false, length = 200)
    private String filter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CCC_CLIENT_ID")
    @NotNull
    private CCCClient cccClient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DATA_SET_VIEW_ID")
    @NotNull
    private DataSetView dataSetView;

    public DataSetView getDataSetView() {
        return dataSetView;
    }

    public void setDataSetView(DataSetView dataSetView) {
        this.dataSetView = dataSetView;
    }

    public CCCClient getCccClient() {
        return cccClient;
    }

    public void setCccClient(CCCClient cccClient) {
        this.cccClient = cccClient;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getSort_index() {
        return sort_index;
    }

    public void setSort_index(Integer sort_index) {
        this.sort_index = sort_index;
    }
}