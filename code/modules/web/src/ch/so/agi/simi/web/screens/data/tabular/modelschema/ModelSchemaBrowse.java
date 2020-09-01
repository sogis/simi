package ch.so.agi.simi.web.screens.data.tabular.modelschema;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.tabular.ModelSchema;

@UiController("simi_ModelSchema.browse")
@UiDescriptor("model-schema-browse.xml")
@LookupComponent("modelSchemasTable")
@LoadDataBeforeShow
public class ModelSchemaBrowse extends StandardLookup<ModelSchema> {
}