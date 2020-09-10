package ch.so.agi.simi.web.screens.data.tabular.postgrestable;

import ch.so.agi.simi.entity.data.tabular.ModelSchema;
import ch.so.agi.simi.entity.data.tabular.TableField;
import ch.so.agi.simi.entity.data.tabular.schemareader.TableAndFieldInfo;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.tabular.PostgresTable;

import javax.inject.Inject;
import java.util.Optional;

@UiController("simiData_PostgresTable.edit")
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
    @Inject
    private CollectionPropertyContainer<TableField> tableFieldsDc;

    @Subscribe("readFromServiceBtn")
    public void onReadFromServiceBtnClick(Button.ClickEvent event) {
        PostgresTable postgresTable = postgresTableDc.getItem();
        SchemaReaderScreen readFromServiceScreen = screenBuilders.screen(this)
                .withScreenClass(SchemaReaderScreen.class)
                .withAfterCloseListener(afterScreenCloseEvent -> {
                    SchemaReaderScreen schemaReaderScreen = afterScreenCloseEvent.getScreen();
                    if (afterScreenCloseEvent.closedWith(StandardOutcome.COMMIT)) {
                        TableAndFieldInfo tableAndFieldInfo = schemaReaderScreen.getResult();
                        notifications.create().withCaption("Result: " + tableAndFieldInfo).show();
                    }
                })
                .build();
        readFromServiceScreen.setSchema(Optional.of(postgresTable).map(PostgresTable::getModelSchema).map(ModelSchema::getSchemaName).orElse(null));
        readFromServiceScreen.setTable(postgresTable.getTableName());
        readFromServiceScreen.setPostgresDB(Optional.of(postgresTable).map(PostgresTable::getModelSchema).map(ModelSchema::getPostgresDB).orElse(null));
        readFromServiceScreen.show();
    }

    @Install(to = "tableFieldsTable.viewFieldsCount", subject = "columnGenerator")
    private Component tableFieldsTableViewFieldsCountColumnGenerator(TableField tableField) {
        return new Table.PlainTextCell(Integer.toString(tableField.getViewFields().size()));
    }
}
