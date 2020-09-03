package ch.so.agi.simi.web.screens.data.tabular.postgrestable;

import ch.so.agi.simi.entity.data.tabular.ModelSchema;
import ch.so.agi.simi.entity.data.tabular.PostgresTableFromService;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.tabular.PostgresTable;

import javax.inject.Inject;
import java.util.Optional;

@UiController("simi_PostgresTable.edit")
@UiDescriptor("postgres-table-edit.xml")
@EditedEntityContainer("postgresTableDc")
@LoadDataBeforeShow
public class PostgresTableEdit extends StandardEditor<PostgresTable> {
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Notifications notifications;
    @Inject
    private InstanceContainer<PostgresTable> postgresTableDc;
    @Inject
    private MetadataTools metadataTools;

    @Subscribe("readFromServiceBtn")
    public void onReadFromServiceBtnClick(Button.ClickEvent event) {
        PostgresTable postgresTable = postgresTableDc.getItem();
        PostgresTableReadFromService readFromServiceScreen = screenBuilders.lookup(PostgresTableFromService.class, this)
                .withScreenClass(PostgresTableReadFromService.class)
                .withSelectHandler(postgresTableFromServices -> {
                    PostgresTableFromService postgresTableFromService = postgresTableFromServices.iterator().next();
                    notifications.create().withCaption("Selected: " + metadataTools.getInstanceName(postgresTableFromService)).show();
                })
                .build();
        readFromServiceScreen.setSchema(Optional.of(postgresTable).map(PostgresTable::getModelSchema).map(ModelSchema::getSchemaName).orElse(null));
        readFromServiceScreen.setTable(postgresTable.getTableName());
        readFromServiceScreen.setPostgresDB(Optional.of(postgresTable).map(PostgresTable::getModelSchema).map(ModelSchema::getPostgresDB).orElse(null));
        readFromServiceScreen.show();
    }
}
