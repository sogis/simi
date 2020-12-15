package ch.so.agi.simi.web.screens.data.externaltable;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.ExternalTable;

@UiController("simiData_ExternalTable.edit")
@UiDescriptor("external-table-edit.xml")
@EditedEntityContainer("externalTableDc")
@LoadDataBeforeShow
public class ExternalTableEdit extends StandardEditor<ExternalTable> {
}