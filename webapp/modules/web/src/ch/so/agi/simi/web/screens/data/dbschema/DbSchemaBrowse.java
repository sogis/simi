package ch.so.agi.simi.web.screens.data.dbschema;

import ch.so.agi.simi.entity.data.DbSchema;
import com.haulmont.cuba.gui.screen.*;

@UiController("simiData_DbSchema.browse")
@UiDescriptor("db-schema-browse.xml")
@LookupComponent("modelSchemasTable")
@LoadDataBeforeShow
public class DbSchemaBrowse extends StandardLookup<DbSchema> {
}