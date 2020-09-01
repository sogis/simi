package ch.so.agi.simi.web.screens.data.tabular.postgresdb;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.tabular.PostgresDB;

@UiController("simi_PostgresDB.edit")
@UiDescriptor("postgres-db-edit.xml")
@EditedEntityContainer("postgresDBDc")
@LoadDataBeforeShow
public class PostgresDBEdit extends StandardEditor<PostgresDB> {
}