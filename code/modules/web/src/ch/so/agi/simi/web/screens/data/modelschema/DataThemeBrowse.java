package ch.so.agi.simi.web.screens.data.modelschema;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.DataTheme;

@UiController("simiData_DataTheme.browse")
@UiDescriptor("data-theme-browse.xml")
@LookupComponent("modelSchemasTable")
@LoadDataBeforeShow
public class DataThemeBrowse extends StandardLookup<DataTheme> {
}