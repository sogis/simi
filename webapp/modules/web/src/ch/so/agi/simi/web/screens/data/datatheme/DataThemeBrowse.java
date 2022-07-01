package ch.so.agi.simi.web.screens.data.datatheme;

import ch.so.agi.simi.entity.data.DbSchema;
import com.haulmont.cuba.gui.screen.*;

@UiController("simiData_DataTheme.browse")
@UiDescriptor("data-theme-browse.xml")
@LookupComponent("modelSchemasTable")
@LoadDataBeforeShow
public class DataThemeBrowse extends StandardLookup<DbSchema> {
}