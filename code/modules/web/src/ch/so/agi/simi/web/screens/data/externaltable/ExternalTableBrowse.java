package ch.so.agi.simi.web.screens.data.externaltable;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.ExternalTable;

@UiController("simiData_ExternalTable.browse")
@UiDescriptor("external-table-browse.xml")
@LookupComponent("externalTablesTable")
@LoadDataBeforeShow
public class ExternalTableBrowse extends StandardLookup<ExternalTable> {
}