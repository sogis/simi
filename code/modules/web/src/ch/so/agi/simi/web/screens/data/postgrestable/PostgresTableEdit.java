package ch.so.agi.simi.web.screens.data.postgrestable;

import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.data.TableField;
import ch.so.agi.simi.web.beans.datatheme.SchemaReaderClientBean;
import ch.so.agi.simi.web.beans.datatheme.ThemeReaderBean;
import ch.so.agi.simi.web.beans.datatheme.reader_dto.update_dto.FieldSyncState;
import ch.so.agi.simi.web.beans.datatheme.reader_dto.update_dto.SyncedField;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
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

    @Subscribe("readFromServiceBtn")
    public void onReadFromServiceBtnClick(Button.ClickEvent event) {
        PostgresTable table = postgresTableDc.getItem();

        List<SyncedField> synced = bean.actualizeWithDbCat(client, table);

        context.merge(table.getTableFields());

        for (SyncedField f : synced) {
            if (f.getSyncState() == FieldSyncState.NEW) {
                tableFieldsDc.getMutableItems().add(f.getTableField());
            }
        }

        notifications.create()
                .withPosition(Notifications.Position.BOTTOM_CENTER)
                .withDescription("Einlesen der Metainformationen abgeschlossen")
                .show();

        /*
        Questions:
        - Must I deal with the "commit state" of the entites (inserted, updated, deleted)?
        - How can I refresh the generic ui table without modifying the "commit state"?

        if(table.getTableFields() != null)
            tableFieldsDc.getMutableItems().removeAll(table.getTableFields());

        List<TableField> inPlaceUpdated = bean.actualizeWithDbCat(client, table);


        if(inPlaceUpdated != null) {
            for (TableField tf : inPlaceUpdated) {
                context.setModified(tf, true);
            }
        }

        if(table.getTableFields() != null)
            tableFieldsDc.getMutableItems().addAll(table.getTableFields());
        */
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
            dialogs.createMessageDialog().withMessage("Dialog kann nicht geschlossen werden weil nicht alle Tabellenfelder synchronisiert sind. \nNicht synchronisierte Felder müssen gelöscht werden.").show();
            event.preventCommit();
        }
    }
}
