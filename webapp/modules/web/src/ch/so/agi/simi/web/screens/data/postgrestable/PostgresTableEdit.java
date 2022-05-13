package ch.so.agi.simi.web.screens.data.postgrestable;

import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.data.TableField;
import ch.so.agi.simi.web.beans.datatheme.SchemaReaderClientBean;
import ch.so.agi.simi.web.beans.datatheme.ThemeReaderBean;
import ch.so.agi.simi.web.beans.datatheme.reader_dto.update_dto.SyncedField;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    DataManager dataManager;

    @Subscribe("readFromServiceBtn")
    public void onReadFromServiceBtnClick(Button.ClickEvent event) {

        PostgresTable table = postgresTableDc.getItem();

        boolean nameNullOrEmpty = table.getTableName() == null || table.getTableName().isEmpty();
        if(table.getDataTheme() == null || nameNullOrEmpty){
            notifications.create()
                    .withPosition(Notifications.Position.MIDDLE_CENTER)
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
                .withDescription("Einlesen der Metainformationen abgeschlossen")
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
            dialogs.createMessageDialog().withMessage("Dialog kann nicht geschlossen werden weil nicht alle Tabellenfelder synchronisiert sind. \nNicht synchronisierte Felder müssen gelöscht werden.").show();
            event.preventCommit();
            return;
        }
        /*
        if (!tableFieldsDc.getItems().stream().allMatch(TableField::getCatSynced)) {
            dialogs.createMessageDialog().withMessage("Dialog kann nicht geschlossen werden weil nicht alle Tabellenfelder synchronisiert sind. \nNicht synchronisierte Felder müssen gelöscht werden.").show();
            event.preventCommit();
            return;
        }

        //Exec deletes first to prevent uk violation for fields keeping their name but changing their datatype
        commitDeletes();

        Reihenfolge zum verhindern von UK-Violation
        - Maske öffnen
        - Einlesen
        - Merken, welche Attribute obsolet sind (in der geo-db nicht mehr vorkommen)
        - Maske ohne Speichern schliessen
        - Maske öffnen
        - obsolete Attribute löschen
        - Maske mit Speichern schliessen
        - Maske öffnen
        - Einlesen
        - (Aliase setzen, ...)
        - Maske mit Speichern schliessen

         */
    }

    private void commitDeletes(){
        Set<Entity> deletes = context.getRemoved();
        if(deletes == null || deletes.size() == 0)
            return;

        CommitContext delCon = new CommitContext();
        for(Entity e : deletes){
            context.evict(e);
            delCon.addInstanceToRemove(e);
        }

        dataManager.commit(delCon);
    }
}
