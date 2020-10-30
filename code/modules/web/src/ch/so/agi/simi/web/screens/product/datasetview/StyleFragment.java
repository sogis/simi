package ch.so.agi.simi.web.screens.product.datasetview;

import ch.so.agi.simi.entity.product.DataSetView;
import ch.so.agi.simi.web.StyleUploadDownloadBean;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UiController("simi_StyleFragment")
@UiDescriptor("style-fragment.xml")
public class StyleFragment extends ScreenFragment {
    private InstanceContainer<DataSetView> dataSetViewDc;

    private String styleProperty;

    private String styleChangedProperty;

    private String fileSuffix;

    @Inject
    private StyleUploadDownloadBean styleUploadDownloadBean;
    @Inject
    private FileUploadField uploadStyleBtn;
    @Inject
    private TimeSource timeSource;
    @Inject
    private Label<String> styleLabel;
    @Inject
    private Notifications notifications;
    @Inject
    private Dialogs dialogs;

    @Subscribe("downloadStyleBtn")
    public void onDownloadStyleBtnClick(Button.ClickEvent event) {
        DataSetView dataSetView = dataSetViewDc.getItem();
        styleUploadDownloadBean.downloadString(dataSetView.getValue(styleProperty), dataSetView.getIdentifier() + fileSuffix);
    }

    @Subscribe("uploadStyleBtn")
    public void onUploadStyleBtnFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) {
        try {
            String fileContent = styleUploadDownloadBean.checkUpload(uploadStyleBtn.getFileContent());

            // checkUpload did not throw an exception, set the property
            DataSetView dataSetView = dataSetViewDc.getItem();
            dataSetView.setValue(styleProperty, fileContent);
            dataSetView.setValue(styleChangedProperty, timeSource.now().toLocalDateTime());

            notifications.create()
                    .withCaption(uploadStyleBtn.getFileName() + " hochgeladen")
                    .show();

            refreshStyleLabel();

        } catch (StyleUploadDownloadBean.StyleUploadException e) {
            dialogs.createMessageDialog()
                    .withCaption("Upload")
                    .withMessage(e.getLocalizedMessage())
                    .withType(Dialogs.MessageType.WARNING)
                    .show();
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

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }
}