package ch.so.agi.simi.web.screens.theme.theme;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.Theme;

@UiController("simiTheme_Theme.browse")
@UiDescriptor("theme-browse.xml")
@LookupComponent("themesTable")
@LoadDataBeforeShow
public class ThemeBrowse extends StandardLookup<Theme> {
}