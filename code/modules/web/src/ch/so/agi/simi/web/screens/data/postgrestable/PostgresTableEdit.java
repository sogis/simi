package ch.so.agi.simi.web.screens.data.postgrestable;

import ch.so.agi.simi.entity.data.DataTheme;
import ch.so.agi.simi.entity.data.PostgresDB;
import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.data.TableField;
import ch.so.agi.simi.entity.data.schemareader.TableAndFieldInfo;
import ch.so.agi.simi.entity.data.schemareader.TableInfo;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@UiController("simiData_PostgresTable.edit")
@UiDescriptor("postgres-table-edit.xml")
@EditedEntityContainer("postgresTableDc")
@LoadDataBeforeShow
public class PostgresTableEdit extends StandardEditor<PostgresTable> {
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private InstanceContainer<PostgresTable> postgresTableDc;
    @Inject
    private CollectionPropertyContainer<TableField> tableFieldsDc;
    @Inject
    private DataContext dataContext;
    @Inject
    private DataManager dataManager;
    @Inject
    private Dialogs dialogs;

    @Subscribe("readFromServiceBtn")
    public void onReadFromServiceBtnClick(Button.ClickEvent event) {
        PostgresTable postgresTable = postgresTableDc.getItem();
        SchemaReaderScreen readFromServiceScreen = screenBuilders.screen(this)
                .withScreenClass(SchemaReaderScreen.class)
                .withAfterCloseListener(afterScreenCloseEvent -> {
                    SchemaReaderScreen schemaReaderScreen = afterScreenCloseEvent.getScreen();
                    if (afterScreenCloseEvent.closedWith(StandardOutcome.COMMIT)) {
                        TableAndFieldInfo tableAndFieldInfo = schemaReaderScreen.getResult();
                        updateTableEntities(tableAndFieldInfo);
                    }
                })
                .build();
        readFromServiceScreen.setSchema(Optional.of(postgresTable).map(PostgresTable::getDataTheme).map(DataTheme::getSchemaName).orElse(null));
        readFromServiceScreen.setTable(postgresTable.getTableName());
        readFromServiceScreen.setPostgresDB(Optional.of(postgresTable).map(PostgresTable::getDataTheme).map(DataTheme::getPostgresDB).orElse(null));
        readFromServiceScreen.show();
    }

    @Install(to = "tableFieldsTable.viewFieldsCount", subject = "columnGenerator")
    private Component tableFieldsTableViewFieldsCountColumnGenerator(TableField tableField) {
        String count;
        if (tableField.getViewFields() == null) {
            count = "0";
        } else {
            count = Integer.toString(tableField.getViewFields().size());
        }
        return new Table.PlainTextCell(count);
    }

    private void updateTableEntities(TableAndFieldInfo tableAndFieldInfo) {
        PostgresTable postgresTable = postgresTableDc.getItem();
        PostgresDB postgresDB = tableAndFieldInfo.getPostgresDB();
        TableInfo tableInfo = tableAndFieldInfo.getTableInfo();

        // tableInfo
        postgresTable.setDataTheme(dataManager.load(DataTheme.class)
                .query("e.schemaName like :name and e.postgresDB = :db")
                .parameter("name", tableInfo.getSchemaName())
                .parameter("db", postgresDB)
                .optional()
                .orElseGet(() -> {
                    DataTheme schema = dataContext.create(DataTheme.class);
                    schema.setPostgresDB(postgresDB);
                    schema.setSchemaName(tableInfo.getSchemaName());

                    return schema;
                }));
        postgresTable.setTableName(tableInfo.getTvName());
        postgresTable.setDescriptionModel(tableInfo.getDescription());

        // tableAndFieldInfo
        postgresTable.setCatSyncStamp(tableAndFieldInfo.getCatSyncStamp());

        // tableFields
        // set all catSynced to false
        if (postgresTable.getTableFields() == null) {
            postgresTable.setTableFields(new ArrayList<>());
        }

        postgresTable.getTableFields().forEach(tableField -> tableField.setCatSynced(false));

        // for every tableField in tableAndFieldInfo create or update existing and set catSynced to true
        tableAndFieldInfo.getFields().forEach(fieldInfo -> {
            TableField tableField = postgresTable.getTableFields().stream()
                    .filter(tf -> Objects.equals(tf.getName(), fieldInfo.getName()) && Objects.equals(tf.getTypeName(), fieldInfo.getType()))
                    .findAny()
                    .orElseGet(() -> {
                        // create new
                        TableField newTableField = dataContext.create(TableField.class);
                        newTableField.setPostgresTable(postgresTable);
                        newTableField.setName(fieldInfo.getName());
                        newTableField.setTypeName(fieldInfo.getType());

                        postgresTable.getTableFields().add(newTableField);
                        tableFieldsDc.getMutableItems().add(newTableField);
                        return newTableField;
                    });

            tableField.setCatSynced(true);
            tableField.setMandatory(fieldInfo.getMandatory());
            tableField.setStrLength(fieldInfo.getLength());
            tableField.setDescriptionModel(fieldInfo.getDescription());
        });

        // geo fields
        tableAndFieldInfo.getFields().stream()
                .filter(fieldInfo -> fieldInfo.getGeoFieldType() != null)
                .findFirst()
                .ifPresent(fieldInfo -> {
                    postgresTable.setGeoFieldName(fieldInfo.getGeoFieldSrOrg());
                    postgresTable.setGeoType(fieldInfo.getGeoFieldType());
                    postgresTable.setGeoEpsgCode(fieldInfo.getGeoFieldSrId());
                });
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        if (!tableFieldsDc.getItems().stream().allMatch(TableField::getCatSynced)) {
            dialogs.createMessageDialog().withMessage("Dialog kann nicht geschlossen werden weil nicht alle Tabellenfelder synchronisiert sind. \nNicht synchronisierte Felder müssen gelöscht werden.").show();
            event.preventCommit();
        }
    }
}
