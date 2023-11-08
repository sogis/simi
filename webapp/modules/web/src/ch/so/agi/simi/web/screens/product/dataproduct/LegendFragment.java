package ch.so.agi.simi.web.screens.product.dataproduct;

import ch.so.agi.simi.entity.product.SingleActor;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

@UiController("simi_LegendFragment")
@UiDescriptor("legend-fragment.xml")
public class LegendFragment extends ScreenFragment {

    private InstanceContainer<SingleActor> singleActorDc;

    @Inject
    private Label<String> legendLabel;
    @Inject
    private FileUploadField legendUploadBtn;
    @Inject
    private Notifications notifications;

    @Subscribe("legendDownloadBtn")
    public void onLegendDownloadBtnClick(Button.ClickEvent event) {
        SingleActor sa = singleActorDc.getItem();

        if(sa.getCustomLegend() == null)
            return;

        byte[] data = sa.getCustomLegend();
        String suffix = "png";


        AppConfig.createExportDisplay(null).show(
                new ByteArrayDataProvider(data),
                sa.getDerivedIdentifier() + "." + suffix
        );
    }

    @Subscribe("legendUploadBtn")
    public void onLegendUploadBtnFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) throws IOException {

        InputStream is = legendUploadBtn.getFileContent();
        byte[] bytes = is.readAllBytes();

        String suffix = extractSuffix();

        SingleActor sa = singleActorDc.getItem();

        sa.setCustomLegend(bytes);
        sa.setCustomLegendSuffix(suffix);

        refreshLegendLabel();
    }

    private String extractSuffix() {
        String suffix = null;
        if(legendUploadBtn.getFileName() != null) {
            String[] parts = legendUploadBtn.getFileName().split(".");

            if(parts != null && parts.length > 1)
                suffix = parts[parts.length - 1].toLowerCase();
        }
        return suffix;
    }

    @Subscribe("legendDeleteBtn")
    public void onLegendDeleteBtnClick(Button.ClickEvent event) {
        SingleActor sa = singleActorDc.getItem();

        sa.setCustomLegend(null);
        sa.setCustomLegendSuffix(null);

        refreshLegendLabel();
    }

    @Subscribe(target = Target.PARENT_CONTROLLER)
    public void onAfterShow(Screen.AfterShowEvent event) {
        refreshLegendLabel();
    }

    private void refreshLegendLabel(){
        SingleActor sa = singleActorDc.getItem();

        if(sa.getCustomLegend() != null){
            legendLabel.setValue("Vorhanden");
        }
        else {
            legendLabel.setValue("Nicht vorh.");
        }
    }

    public InstanceContainer<SingleActor> getSingleActorDc() {
        return singleActorDc;
    }

    public void setSingleActorDc(InstanceContainer<SingleActor> singleActorDc) {
        this.singleActorDc = singleActorDc;
    }
}