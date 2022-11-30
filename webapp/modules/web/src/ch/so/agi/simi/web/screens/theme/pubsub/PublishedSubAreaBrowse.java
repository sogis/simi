package ch.so.agi.simi.web.screens.theme.pubsub;

import ch.so.agi.simi.entity.theme.dbview.ThemePubValidation;
import ch.so.agi.simi.entity.theme.subarea.PublishedSubArea;
import ch.so.agi.simi.web.screens.theme.themepub.ThemePubValidationEdit;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("simiTheme_PublishedSubArea.browse")
@UiDescriptor("published-sub-area-browse.xml")
@LookupComponent("publishedSubAreasTable")
@LoadDataBeforeShow
public class PublishedSubAreaBrowse extends StandardLookup<PublishedSubArea> {
    @Inject
    private ScreenBuilders screenBuilders;

    @Inject
    private CollectionLoader<PublishedSubArea> publishedSubAreasDl;

    @Subscribe("createBtn")
    public void onCreateBtnClick(Button.ClickEvent event) {
        screenBuilders.editor(PublishedSubArea.class, this)
                .withOpenMode(OpenMode.DIALOG)
                .withScreenClass(PublishedSubAreaCreate.class)
                .withAfterCloseListener((afterCloseEvent) -> publishedSubAreasDl.load())
                .show();

        /*
        ThemePubValidationEdit viewScreen = (ThemePubValidationEdit)screenBuilders.editor(ThemePubValidation.class, this)
                .withOpenMode(OpenMode.DIALOG)
                .build();
        viewScreen.setThemePubId(selected.getId());
        viewScreen.setReadOnly(true);
        viewScreen.show();
        */

    }
}