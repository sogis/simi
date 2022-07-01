package ch.so.agi.simi.web.screens.data.dbschema;

import ch.so.agi.simi.entity.data.DbSchema;
import com.haulmont.cuba.gui.screen.*;

@UiController("simiData_DbSchema.edit")
@UiDescriptor("db-schema-edit.xml")
@EditedEntityContainer("modelSchemaDc")
@LoadDataBeforeShow
public class DbSchemaEdit extends StandardEditor<DbSchema> {

}