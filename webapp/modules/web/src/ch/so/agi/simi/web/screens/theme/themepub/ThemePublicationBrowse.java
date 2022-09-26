package ch.so.agi.simi.web.screens.theme.themepub;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.ThemePublication;

@UiController("simiTheme_ThemePublication.browse")
@UiDescriptor("theme-publication-browse.xml")
@LookupComponent("themePublicationsTable")
@LoadDataBeforeShow
public class ThemePublicationBrowse extends StandardLookup<ThemePublication> {
}