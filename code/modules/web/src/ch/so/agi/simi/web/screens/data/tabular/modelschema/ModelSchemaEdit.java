package ch.so.agi.simi.web.screens.data.tabular.modelschema;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.tabular.ModelSchema;

@UiController("simiData_ModelSchema.edit")
@UiDescriptor("model-schema-edit.xml")
@EditedEntityContainer("modelSchemaDc")
@LoadDataBeforeShow
public class ModelSchemaEdit extends StandardEditor<ModelSchema> {
}