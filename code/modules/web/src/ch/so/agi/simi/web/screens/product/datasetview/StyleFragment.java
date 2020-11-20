package ch.so.agi.simi.web.screens.product.datasetview;

import ch.so.agi.simi.entity.product.DataSetView;
import ch.so.agi.simi.entity.product.util.StyleAsset;
import ch.so.agi.simi.web.beans.filetransfer.StyleDbContent;
import ch.so.agi.simi.web.beans.filetransfer.StyleFileContent;
import ch.so.agi.simi.web.beans.filetransfer.StyleStorageBean;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@UiController("simi_StyleFragment")
@UiDescriptor("style-fragment.xml")
public class StyleFragment extends ScreenFragment {

    private InstanceContainer<DataSetView> dataSetViewDc;

    private String styleProperty;

    private String styleChangedProperty;

    @Inject
    private StyleStorageBean styleStorageBean;
    @Inject
    private FileUploadField uploadStyleBtn;
    @Inject
    private TimeSource timeSource;
    @Inject
    private Label<String> styleLabel;
    @Inject
    private Notifications notifications;

    @Subscribe("downloadStyleBtn")
    public void onDownloadStyleBtnClick(Button.ClickEvent event) {
        DataSetView dataSetView = dataSetViewDc.getItem();

        //Create the assets
        HashMap<String, byte[]> assets = new HashMap<>();
        List<StyleAsset> assetList = null;

        if(assetListForContext() != null) {
            for (StyleAsset asset : assetListForContext()) {
                assets.put(asset.getFileName(), asset.getFileContent());
            }
        }

        if(assets.size() == 0)
            assets = null;

        StyleDbContent dbContent = new StyleDbContent(
                dataSetView.getValue(styleProperty),
                assets
        );

        StyleFileContent fileContent = styleStorageBean.transformFieldsToFileContent(dbContent);

        AppConfig.createExportDisplay(null).show(
                new ByteArrayDataProvider(fileContent.getData()),
                buildStyleFileName(fileContent.getFileContentType().asFileSuffix())
        );
    }

    private String buildStyleFileName(String fileSuffix){

        String fileName =  dataSetViewDc.getItem().getIdentifier();

        if(!isServerStyle())
            fileName += "__desktop";

        fileName += fileSuffix;

        return fileName;
    }

    private List<StyleAsset> assetListForContext(){
        List<StyleAsset> assetList = null;

        if(isServerStyle())
            assetList = dataSetViewDc.getItem().getStyleServerAssets();
        else
            assetList = dataSetViewDc.getItem().getStyleClientAssets();

        return assetList;
    }

    private boolean isServerStyle(){
        if(styleProperty.toLowerCase().contains("server"))
            return true;
        else if (styleProperty.toLowerCase().contains("desktop"))
            return false;
        else
            throw new RuntimeException("Could not determine if fragment is configured for server or client style");
    }

    @Subscribe("uploadStyleBtn")
    public void onUploadStyleBtnFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) throws IOException {

        InputStream is = uploadStyleBtn.getFileContent();
        byte[] bytes = is.readAllBytes();

        StyleFileContent styleFileContent = new StyleFileContent(
                bytes,
                StyleStorageBean.FileContentType.forFileName(uploadStyleBtn.getFileName())
        );

        StyleDbContent dbContent = styleStorageBean.transformFileToFields(styleFileContent, new int[]{2,18});

        DataSetView dataSetView = dataSetViewDc.getItem();
        dataSetView.setValue(styleProperty, dbContent.getQmlContent());
        dataSetView.setValue(styleChangedProperty, timeSource.now().toLocalDateTime());

        updateStyleAssets(dbContent.getAssets());

        notifications.create()
                .withCaption(uploadStyleBtn.getFileName() + " hochgeladen")
                .show();

        refreshStyleLabel();
    }

    private void updateStyleAssets(Optional<HashMap<String, byte[]>> assets){

        removeStoredAssets();

        DataContext dataContext = UiControllerUtils.getScreenData(getHostScreen()).getDataContext();

        if(!assets.isPresent())
            return;

        HashMap<String, byte[]> fileAssets = assets.get();
        for(String fileName : fileAssets.keySet()){
            StyleAsset dbAsset = new StyleAsset();

            dbAsset.setDatasetSetView(dataSetViewDc.getItem());
            dbAsset.setFileName(fileName);
            dbAsset.setFileContent(fileAssets.get(fileName));

            dataContext.merge(dbAsset);
        }
    }

    private void removeStoredAssets(){
        DataContext dataContext = UiControllerUtils.getScreenData(getHostScreen()).getDataContext();

        // Must copy to new collection, as dataContext.remove(...) changes the ormList.
        // Iterate over a changing list leeds to unexpected errors
        List<StyleAsset> ormList = assetListForContext();
        if(ormList != null) {
            List<StyleAsset> dbAssets = List.copyOf(ormList);

            for (StyleAsset dbAsset : dbAssets)
                dataContext.remove(dbAsset);
        }
    }

    @Subscribe(target = Target.PARENT_CONTROLLER)
    public void onAfterShow(Screen.AfterShowEvent event) {
        refreshStyleLabel();
    }

    private void refreshStyleLabel() {
        DataSetView dataSetView = dataSetViewDc.getItem();
        if(dataSetView.getValue(styleProperty) != null) {
            LocalDateTime timestamp = dataSetView.getValue(styleChangedProperty);
            if (timestamp != null) {
                styleLabel.setValue("Hochgeladen " + timestamp.format(DateTimeFormatter.ofPattern("dd.MM.yyyy / HH:mm")));
            } else {
                styleLabel.setValue("Hochgeladen Zeitpunkt unbekannt");
            }

        } else {
            styleLabel.setValue("Kein Style");
        }
    }

    public InstanceContainer<DataSetView> getDataSetViewDc() {
        return dataSetViewDc;
    }

    public void setDataSetViewDc(InstanceContainer<DataSetView> dataSetViewDc) {
        this.dataSetViewDc = dataSetViewDc;
    }

    public String getStyleProperty() {
        return styleProperty;
    }

    public void setStyleProperty(String styleProperty) {
        this.styleProperty = styleProperty;
    }

    public String getStyleChangedProperty() {
        return styleChangedProperty;
    }

    public void setStyleChangedProperty(String styleChangedProperty) {
        this.styleChangedProperty = styleChangedProperty;
    }
}