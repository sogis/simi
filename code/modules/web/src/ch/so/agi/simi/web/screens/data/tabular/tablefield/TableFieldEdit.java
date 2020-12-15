package ch.so.agi.simi.web.screens.data.tabular.tablefield;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.TableField;

@UiController("simiData_TableField.edit")
@UiDescriptor("table-field-edit.xml")
@EditedEntityContainer("tableFieldDc")
@LoadDataBeforeShow
public class TableFieldEdit extends StandardEditor<TableField> {
}