package ch.so.agi.simi.web.screens.data.tabular.postgresdb;

import ch.so.agi.simi.entity.data.tabular.TableView;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.actions.list.RemoveAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.tabular.PostgresDB;

import javax.inject.Inject;
import javax.inject.Named;

@UiController("simi_PostgresDB.browse")
@UiDescriptor("postgres-db-browse.xml")
@LookupComponent("postgresDBsTable")
@LoadDataBeforeShow
public class PostgresDBBrowse extends StandardLookup<PostgresDB> {
    @Named("postgresDBsTable.remove")
    private RemoveAction<PostgresDB> postgresDBsTableRemove;
    @Inject
    private Table<PostgresDB> postgresDBsTable;
    @Inject
    private Dialogs dialogs;

    @Subscribe("postgresDBsTable.remove")
    public void onPostgresDBsTableRemove(Action.ActionPerformedEvent event) {
        postgresDBsTableRemove.setConfirmation(false);

        if (postgresDBsTable.getLookupSelectedItems().stream().allMatch(postgresDB -> postgresDB.getModelSchemas().isEmpty())) {
            postgresDBsTableRemove.execute();
        } else {
            dialogs.createMessageDialog(Dialogs.MessageType.WARNING)
                    .withCaption("Löschen")
                    .withMessage("Die ausgewählten PostgresDB enthalten Model Schemas und können daher nicht gelöscht werden!")
                    .show();
        }
    }
}