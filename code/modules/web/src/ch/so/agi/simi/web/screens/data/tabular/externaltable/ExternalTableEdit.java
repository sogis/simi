package ch.so.agi.simi.web.screens.data.tabular.externaltable;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.tabular.ExternalTable;

@UiController("simiData_ExternalTable.edit")
@UiDescriptor("external-table-edit.xml")
@EditedEntityContainer("externalTableDc")
@LoadDataBeforeShow
public class ExternalTableEdit extends StandardEditor<ExternalTable> {
}