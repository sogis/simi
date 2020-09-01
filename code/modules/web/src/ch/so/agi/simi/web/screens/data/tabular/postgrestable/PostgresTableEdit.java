package ch.so.agi.simi.web.screens.data.tabular.postgrestable;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.tabular.PostgresTable;

@UiController("simi_PostgresTable.edit")
@UiDescriptor("postgres-table-edit.xml")
@EditedEntityContainer("postgresTableDc")
@LoadDataBeforeShow
public class PostgresTableEdit extends StandardEditor<PostgresTable> {
}