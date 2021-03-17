package ch.so.agi.simi.web.screens.data.modelschema;

import ch.so.agi.simi.entity.data.DataTheme;
import com.haulmont.cuba.gui.screen.*;

@UiController("simiData_DataTheme.edit")
@UiDescriptor("data-theme-edit.xml")
@EditedEntityContainer("modelSchemaDc")
@LoadDataBeforeShow
public class DataThemeEdit extends StandardEditor<DataTheme> {
}