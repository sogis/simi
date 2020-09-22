package ch.so.agi.simi.entity.ccc;

import ch.so.agi.simi.entity.SimiStandardEntity;
import ch.so.agi.simi.entity.product.DataSetView;
import com.haulmont.chile.core.annotations.NamePattern;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMICCC_NOTIFY_LAYER", uniqueConstraints = {
        @UniqueConstraint(name = "IDX_SIMI_NOTIFY_LAYER_UNQ_CCC_CLIENT_ID_DATA_SET_VIEW_ID", columnNames = {"CCC_CLIENT_ID", "DATA_SET_VIEW_ID"})
})
@Entity(name = "simiCCC_NotifyLayer")
@NamePattern("%s|title")
public class NotifyLayer extends SimiStandardEntity {
    private static final long serialVersionUID = -2836231800251361499L;

    @NotNull
    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @NotNull
    @Lob
    @Column(name = "ATTRIBUTE_MAP", nullable = false)
    private String attributeMap;

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

    public String getAttributeMap() {
        return attributeMap;
    }

    public void setAttributeMap(String attributeMap) {
        this.attributeMap = attributeMap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}