package ch.so.agi.simi.web.screens.theme.filetype;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.CustomFileType;

@UiController("simiTheme_CustomFileType.browse")
@UiDescriptor("custom-file-type-browse.xml")
@LookupComponent("customFileTypesTable")
@LoadDataBeforeShow
public class CustomFileTypeBrowse extends StandardLookup<CustomFileType> {
}