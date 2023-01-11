package ch.so.agi.simi.web.screens.theme.themepub;

import ch.so.agi.simi.core.service.pub.GenerateThemePubDocService;
import ch.so.agi.simi.core.service.pub.UpdatePublishTimeService;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.entity.theme.dbview.ThemePubValidation;
import ch.so.agi.simi.entity.theme.subarea.PublishedSubArea;
import ch.so.agi.simi.entity.theme.subarea.SubArea;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.BrowserFrame;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.StreamResource;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@UiController("simi_ThemePubDatasheet")
@UiDescriptor("theme-pub-datasheet.xml")
public class ThemePubDatasheet extends Screen {

    private UUID themePubId;
    private byte[] htmlContentUtf8;
    private String tpIdent;

    @Inject
    protected UserSessionSource userSessionSource;

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

    @Inject
    private Metadata metadata;

    @Inject
    private ExportDisplay exportDisplay;

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
        tpIdent = tp.deferFullIdent();

        if(needsDummy(tp)){
            if(!canCreateDummy()){
                notifications.create(Notifications.NotificationType.TRAY)
                        .withCaption("Dummy-Publikation")
                        .withDescription("Keine Berechtigung für die Erstellung der Dummy-Publikation..")
                        .show();

                return;
            }
            else{
                notifications.create(Notifications.NotificationType.TRAY)
                        .withCaption("Dummy-Publikation")
                        .withDescription("Erstelle Dummy-Publikation\nmit Datenabdeckung \"kanton\"\nund lade Datenblatt...")
                        .show();

                LocalDateTime dummy = LocalDateTime.of(1111, 11, 11, 11, 11, 11);
                updateService.update(tpIdent, SubArea.KTSO_COVERAGE_IDENTIFIER, dummy);
            }
        }
        else{
            notifications.create(Notifications.NotificationType.TRAY).withCaption("Lade Datenblatt...").show();
        }

        String htmlDoc = docService.generateDoc(tpIdent);
        htmlContentUtf8 = htmlDoc.getBytes(StandardCharsets.UTF_8);

        browserFrame.setSource(StreamResource.class)
                .setStreamSupplier(() -> new ByteArrayInputStream(htmlContentUtf8))
                .setMimeType("text/html");

    }

    @Subscribe("btnSheetDownload")
    public void onBtnSheetDownloadClick(Button.ClickEvent event) {
        String filename = tpIdent.replaceAll("\\.", "_") + "_datenbeschreibung.html";

        exportDisplay.show(
                new ByteArrayDataProvider(htmlContentUtf8),
                filename,
                ExportFormat.OCTET_STREAM); // Set intentionally to stream to trigger direct download in browsers instead of display in new window
    }

    private boolean needsDummy(ThemePublication tp){
        return tp.getPublishedSubAreas() == null || tp.getPublishedSubAreas().size() == 0;
    }

    private boolean canCreateDummy(){
        UserSession s = userSessionSource.getUserSession();
        return s.isEntityOpPermitted(metadata.getClassNN(PublishedSubArea.NAME), EntityOp.CREATE);
    }

    public void setThemePubId(UUID themePubId) {
        this.themePubId = themePubId;
    }
}