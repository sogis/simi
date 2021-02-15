package ch.so.agi.simi.entity.product.datasetview;

import ch.so.agi.simi.entity.SimiEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "SIMI_STYLE_ASSET")
@Entity(name = "simi_StyleAsset")
public class StyleAsset extends SimiEntity {
    private static final long serialVersionUID = 5535641619105786448L;

    @JoinColumn(name = "DATASET_SET_VIEW_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private DataSetView datasetSetView;

    @NotNull
    @Column(name = "IS_FOR_SERVER", nullable = false)
    private Boolean isForServer = false;

    @NotNull
    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    @NotNull
    @Column(name = "FILE_CONTENT", nullable = false)
    private byte[] fileContent;

    public Boolean getIsForServer() {
        return isForServer;
    }

    public void setIsForServer(Boolean isForServer) {
        this.isForServer = isForServer;
    }

    public DataSetView getDatasetSetView() {
        return datasetSetView;
    }

    public void setDatasetSetView(DataSetView datasetSetView) {
        this.datasetSetView = datasetSetView;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}