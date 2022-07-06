package ch.so.agi.simi.web.screens.data.datasetviews.datasetview;

import ch.so.agi.simi.entity.data.datasetview.DataSetView;
import ch.so.agi.simi.entity.data.datasetview.StyleAsset;
import ch.so.agi.simi.web.beans.style.StyleDbContent;
import ch.so.agi.simi.web.beans.style.StyleFileContent;
import ch.so.agi.simi.web.beans.style.StyleStorageBean;
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
import java.util.LinkedList;
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
    private FileUploadField styleUploadBtn;
    @Inject
    private TimeSource timeSource;
    @Inject
    private Label<String> styleLabel;
    @Inject
    private Notifications notifications;

    @Subscribe("styleUploadBtn")
    public void onStyleUploadBtnFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) throws IOException {
        InputStream is = styleUploadBtn.getFileContent();
        byte[] bytes = is.readAllBytes();

        StyleFileContent styleFileContent = new StyleFileContent(
                bytes,
                StyleStorageBean.FileContentType.forFileName(styleUploadBtn.getFileName())
        );

        StyleDbContent dbContent = styleStorageBean.transformFileToFields(styleFileContent, new int[]{2,18});

        DataSetView dataSetView = dataSetViewDc.getItem();
        dataSetView.setValue(styleProperty, dbContent.getQmlContent());
        dataSetView.setValue(styleChangedProperty, timeSource.now().toLocalDateTime());

        updateStyleAssets(dbContent.getAssets());

        refreshStyleLabel();
    }

    @Subscribe("styleDownloadBtn")
    public void onStyleDownloadBtnClick(Button.ClickEvent event) {

        DataSetView dataSetView = dataSetViewDc.getItem();
        String qmlContent = dataSetView.getValue(styleProperty);

        if(qmlContent == null || qmlContent.length() == 0)
            return;

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
                qmlContent,
                assets
        );

        StyleFileContent fileContent = styleStorageBean.transformFieldsToFileContent(dbContent);

        AppConfig.createExportDisplay(null).show(
                new ByteArrayDataProvider(fileContent.getData()),
                buildStyleFileName(fileContent.getFileContentType().asFileSuffix())
        );
    }

    @Subscribe("styleDeleteBtn")
    public void onStyleDeleteBtnClick(Button.ClickEvent event) {
        DataSetView dataSetView = dataSetViewDc.getItem();
        dataSetView.setValue(styleProperty, null);
        dataSetView.setValue(styleChangedProperty, null);

        removeStoredAssets();

        refreshStyleLabel();
    }

    private String buildStyleFileName(String fileSuffix){

        String fileName =  dataSetViewDc.getItem().getDerivedIdentifier();

        if(!isServerStyle())
            fileName += "__desktop";

        fileName += fileSuffix;

        return fileName;
    }

    private List<StyleAsset> assetListForContext(){

        // Must copy to new collection, as dataContext.remove(...) changes the ormList.
        // Iterate over a changing list leeds to unexpected errors
        List<StyleAsset> ormList = dataSetViewDc.getItem().getStyleAssets();

        if(ormList == null || ormList.size() == 0)
            return null;

        List<StyleAsset> listForContext = new LinkedList<>();
        boolean isForServer = isServerStyle();

        for(StyleAsset asset : ormList){
            if(isForServer == asset.getIsForServer())
                listForContext.add(asset);
        }

        return listForContext;
    }

    private boolean isServerStyle(){
        if(styleProperty.toLowerCase().contains("server"))
            return true;
        else if (styleProperty.toLowerCase().contains("desktop"))
            return false;
        else
            throw new RuntimeException("Could not determine if fragment is configured for server or client style");
    }

    private void updateStyleAssets(Optional<HashMap<String, byte[]>> assets){

        removeStoredAssets();

        if(!assets.isPresent())
            return;

        DataContext dataContext = UiControllerUtils.getScreenData(getHostScreen()).getDataContext();

        boolean isForServer = isServerStyle();
        HashMap<String, byte[]> fileAssets = assets.get();
        for(String fileName : fileAssets.keySet()){
            StyleAsset dbAsset = new StyleAsset();

            dbAsset.setDatasetSetView(dataSetViewDc.getItem());
            dbAsset.setFileName(fileName);
            dbAsset.setFileContent(fileAssets.get(fileName));
            dbAsset.setIsForServer(isForServer);

            dataContext.merge(dbAsset);
        }
    }

    private void removeStoredAssets(){
        DataContext dataContext = UiControllerUtils.getScreenData(getHostScreen()).getDataContext();

        List<StyleAsset> contextList = assetListForContext();
        if(contextList == null)
            return;

        for (StyleAsset dbAsset : contextList)
            dataContext.remove(dbAsset);
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