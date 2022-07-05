package ch.so.agi.simi.web.screens.theme;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.Theme;

@UiController("simiTheme_Theme.edit")
@UiDescriptor("theme-edit.xml")
@EditedEntityContainer("themeDc")
@LoadDataBeforeShow
public class ThemeEdit extends StandardEditor<Theme> {
}