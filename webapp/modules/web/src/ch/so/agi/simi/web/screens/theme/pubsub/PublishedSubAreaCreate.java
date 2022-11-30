package ch.so.agi.simi.web.screens.theme.pubsub;

import ch.so.agi.simi.core.service.pub.UpdatePublishTimeService;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.subarea.PublishedSubArea;

import javax.inject.Inject;
import java.time.LocalDateTime;

@UiController("simiTheme_PublishedSubArea.create")
@UiDescriptor("published-sub-area-create.xml")
@EditedEntityContainer("publishedSubAreaDc")
@LoadDataBeforeShow
public class PublishedSubAreaCreate extends StandardEditor<PublishedSubArea> {
    @Inject
    private UpdatePublishTimeService updateService;

    @Inject
    private InstanceContainer<PublishedSubArea> publishedSubAreaDc;

    @Subscribe("commitAndCloseBtn")
    public void onCommitAndCloseBtnClick(Button.ClickEvent event) {

        PublishedSubArea edited = publishedSubAreaDc.getItem();
        if(edited.getThemePublication() == null)
            return;

        LocalDateTime published = edited.getPublished();
        if(published == null)
            published = LocalDateTime.now();

        updateService.update(edited.getThemePublication().deferFullIdent(), edited.getThemePublication().getCoverageIdent(), published);

        this.close(StandardOutcome.DISCARD);

        //https://doc.cuba-platform.com/manual-latest/opening_screens.html .withAfterCloseListener
    }

    @Subscribe("closeBtn")
    public void onCCloseBtnClick(Button.ClickEvent event) {
        this.close(StandardOutcome.CLOSE);
        //https://doc.cuba-platform.com/manual-latest/opening_screens.html .withAfterCloseListener
    }
}