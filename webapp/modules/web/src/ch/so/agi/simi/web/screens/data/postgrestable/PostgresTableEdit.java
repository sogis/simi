package ch.so.agi.simi.web.screens.data.postgrestable;

import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.data.TableField;
import ch.so.agi.simi.web.beans.datatheme.SchemaReaderClientBean;
import ch.so.agi.simi.web.beans.datatheme.ThemeReaderBean;
import ch.so.agi.simi.web.beans.datatheme.reader_dto.update_dto.SyncedField;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("simiData_PostgresTable.edit")
@UiDescriptor("postgres-table-edit.xml")
@EditedEntityContainer("postgresTableDc")
@LoadDataBeforeShow
public class PostgresTableEdit extends StandardEditor<PostgresTable> {
    @Inject
    private InstanceContainer<PostgresTable> postgresTableDc;
    @Inject
    private CollectionPropertyContainer<TableField> tableFieldsDc;
    @Inject
    private Dialogs dialogs;
    @Inject
    private ThemeReaderBean bean;
    @Inject
    private SchemaReaderClientBean client;
    @Inject
    DataContext context;
    @Inject
    private Notifications notifications;
    @Inject
    private ScreenBuilders screenBuilders;


    @Subscribe("readFromServiceBtn")
    public void onReadFromServiceBtnClick(Button.ClickEvent event) {

        PostgresTable table = postgresTableDc.getItem();

        boolean nameNullOrEmpty = table.getTableName() == null || table.getTableName().isEmpty();
        if(table.getDataTheme() == null || nameNullOrEmpty){
            notifications.create()
                    .withPosition(Notifications.Position.MIDDLE_CENTER)
                    .withCaption("Einlesen nicht möglich")
                    .withDescription("Datenthema und Tabellenname muss vor dem Auslesen gesetzt werden.")
                    .show();

            return;
        }

        List<SyncedField> synced = bean.actualizeWithDbCat(client, table);

        List<TableField> uiExposedFields = tableFieldsDc.getMutableItems();

        uiExposedFields.clear();

        List<TableField> mergedList = new ArrayList<>();
        for(SyncedField sf : synced){
            TableField merged = context.merge(sf.getTableField());
            mergedList.add(merged);
        }

        uiExposedFields.addAll(mergedList);

        notifications.create()
                .withPosition(Notifications.Position.BOTTOM_CENTER)
                .withCaption("Einlesen der Metainformationen abgeschlossen")
                .show();
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

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        if (!tableFieldsDc.getItems().stream().allMatch(TableField::getCatSynced)) {
            dialogs.createMessageDialog()
                    .withCaption("Speichern nicht möglich.")
                    .withMessage("Nicht synchronisierte Felder müssen gelöscht werden.")
                    .show();
            event.preventCommit();
        }
    }

    @Subscribe("reloadFromDb")
    public void onReloadFromDbClick(Button.ClickEvent event) {
        screenBuilders.editor(PostgresTable.class, this)
                .editEntity(this.getEditedEntity())
                .build()
                .show();
        this.closeWithDiscard();
    }
}
