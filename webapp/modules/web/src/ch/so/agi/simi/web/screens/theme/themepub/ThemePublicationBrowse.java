package ch.so.agi.simi.web.screens.theme.themepub;

import ch.so.agi.simi.entity.theme.dbview.ThemePubValidation;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.ThemePublication;

import javax.inject.Inject;

@UiController("simiTheme_ThemePublication.browse")
@UiDescriptor("theme-publication-browse.xml")
@LookupComponent("themePublicationsTable")
@LoadDataBeforeShow
public class ThemePublicationBrowse extends StandardLookup<ThemePublication> {
    @Inject
    private Table<ThemePublication> themePublicationsTable;
    @Inject
    private ScreenBuilders screenBuilders;
    @Subscribe("viewBtn")
    public void onViewBtnClick(Button.ClickEvent event) {

        ThemePublication selected = themePublicationsTable.getSingleSelected();
        if(selected == null)
            return;

        ThemePubValidationEdit viewScreen = (ThemePubValidationEdit)screenBuilders.editor(ThemePubValidation.class, this)
                .withOpenMode(OpenMode.DIALOG)
                .build();
        viewScreen.setThemePubId(selected.getId());
        viewScreen.setReadOnly(true);
        viewScreen.show();
    }
}