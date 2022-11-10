package ch.so.agi.simi.web.screens.theme.themepub;

import ch.so.agi.simi.core.service.pub.GenerateThemePubDocService;
import ch.so.agi.simi.core.service.pub.UpdatePublishTimeService;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.entity.theme.dbview.ThemePubValidation;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.BrowserFrame;
import com.haulmont.cuba.gui.components.StreamResource;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@UiController("simi_ThemePubDatasheet")
@UiDescriptor("theme-pub-datasheet.xml")
public class ThemePubDatasheet extends Screen {
    private UUID themePubId;

    @Inject
    private InstanceLoader<ThemePublication> themePubDl;

    @Inject
    private GenerateThemePubDocService docService;

    @Inject
    private UpdatePublishTimeService updateService;

    @Inject
    BrowserFrame browserFrame;

    @Inject
    private Notifications notifications;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        if(themePubId == null)
            return;

        themePubDl.setEntityId(themePubId);
        themePubDl.load();
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {

        ThemePublication tp = themePubDl.getContainer().getItem();
        String tpIdent = themePubDl.getContainer().getItem().deferFullIdent();

        if(tp.getPublishedSubAreas() == null || tp.getPublishedSubAreas().size() == 0){
            notifications.create(Notifications.NotificationType.TRAY)
                    .withCaption("Dummy-Publikation")
                    .withDescription("Erstelle Dummy-Publikation\nmit Datenabdeckung \"kanton\"\nund lade Datenblatt...")
                    .show();

            LocalDateTime dummy = LocalDateTime.of(1111, 11, 11, 11, 11, 11);
            updateService.linkToDefaultDataCoverage(tpIdent, dummy);
        }
        else{
            notifications.create(Notifications.NotificationType.TRAY).withCaption("Lade Datenblatt...").show();
        }

        try{
            String htmlDoc = docService.generateDoc(tpIdent);
            byte[] bytes = htmlDoc.getBytes(StandardCharsets.UTF_8);

            browserFrame.setSource(StreamResource.class)
                    .setStreamSupplier(() -> new ByteArrayInputStream(bytes))
                    .setMimeType("text/html");
        }
        catch(Exception e){
            notifications.create(Notifications.NotificationType.ERROR).withCaption("Generieren Datenblatt fehlgeschlagen").withDescription(e.getMessage()).show();
        }
    }

    public void setThemePubId(UUID themePubId) {
        this.themePubId = themePubId;
    }


}