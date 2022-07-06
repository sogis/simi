package ch.so.agi.simi.web.screens.theme;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.CustomFileType;

@UiController("simiTheme_CustomFileType.edit")
@UiDescriptor("custom-file-type-edit.xml")
@EditedEntityContainer("customFileTypeDc")
@LoadDataBeforeShow
public class CustomFileTypeEdit extends StandardEditor<CustomFileType> {
}