package ch.so.agi.simi.web.screens.theme.subarea;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.subarea.SubArea;

@UiController("simiTheme_SubArea.browse")
@UiDescriptor("sub-area-browse.xml")
@LookupComponent("subAreasTable")
@LoadDataBeforeShow
public class SubAreaBrowse extends StandardLookup<SubArea> {
}