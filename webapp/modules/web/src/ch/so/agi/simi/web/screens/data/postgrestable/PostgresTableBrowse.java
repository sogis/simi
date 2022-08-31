package ch.so.agi.simi.web.screens.data.postgrestable;

import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.data.datasetview.TableView;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.Set;

@UiController("simiData_PostgresTable.browse")
@UiDescriptor("postgres-table-browse.xml")
@LookupComponent("postgresTablesTable")
@LoadDataBeforeShow
public class PostgresTableBrowse extends StandardLookup<PostgresTable> {

    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Table<PostgresTable> postgresTablesTable;
    @Inject
    private MetadataTools metadataTools;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private CollectionLoader<PostgresTable> postgresTablesDl;
    @Inject
    private Notifications notifications;
    @Inject
    private RemoveOperation removeOperation;

    @Subscribe("postgresTablesTable.createTableView")
    public void onPostgresTablesTableCreateTableView(Action.ActionPerformedEvent event) {
        PostgresTable postgresTable = postgresTablesTable.getSingleSelected();
        Screen editScreen = screenBuilders.editor(TableView.class, this)
                .newEntity()
                .withInitializer(tableView -> {
                    tableView.setPostgresTable(postgresTable);
                })
                .build();

        editScreen.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.closedWith(StandardOutcome.COMMIT)) {
                postgresTablesDl.load();
            }
        });

        editScreen.show();
    }

    @Install(to = "postgresTablesTable.instanceName", subject = "columnGenerator")
    private Component postgresTablesTableInstanceNameColumnGenerator(PostgresTable postgresTable) {
        return new Table.PlainTextCell(metadataTools.getInstanceName(postgresTable));
    }

    @Install(to = "postgresTablesTable.tableViews", subject = "columnGenerator")
    private Component postgresTablesTableTableViewsColumnGenerator(PostgresTable postgresTable) {
        VBoxLayout vBox = uiComponents.create(VBoxLayout.class);
        vBox.setSpacing(true);

        postgresTable.getTableViews().forEach(tableView -> {
            LinkButton linkButton = uiComponents.create(LinkButton.class);
            linkButton.setCaption(tableView.getDerivedIdentifier());
            linkButton.addClickListener(event -> editTableView(tableView));
            vBox.add(linkButton);
        });
        return vBox;
    }

    private void editTableView(TableView tableView) {
        screenBuilders.editor(TableView.class, this)
            .editEntity(tableView)
            .build()
            .show();
    }

    @Subscribe("postgresTablesTable.remove")
    public void onPostgresTablesTableRemove(Action.ActionPerformedEvent event) {

        boolean canDelete = true;

        Set<PostgresTable> tables = postgresTablesTable.getSelected();

        for(PostgresTable tbl : tables){
            if(tbl.getTableViews().size() > 0){
                canDelete = false;
                break;
            }
        }

        if(!canDelete){
            notifications.create(Notifications.NotificationType.TRAY)
                    .withCaption("Löschen nicht möglich")
                    .withDescription("Bitte zuerst die verknüpften Dataset-Views entfernen")
                    .show();
        }
        else{
            removeOperation.builder(postgresTablesTable)
                    .withConfirmation(false)
                    .remove();
        }
    }
}