package ch.so.agi.simi.web.screens.data.postgrestable;

import ch.so.agi.simi.entity.data.PostgresDB;
import ch.so.agi.simi.entity.data.schemareader.TableAndFieldInfo;
import ch.so.agi.simi.entity.data.schemareader.TableListing;
import ch.so.agi.simi.entity.data.schemareader.TableShortInfo;
import ch.so.agi.simi.web.beans.schema.SchemaReaderBean;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.haulmont.cuba.gui.screen.*;
import org.springframework.web.client.RestClientException;

import javax.inject.Inject;
import java.util.Collection;

@UiController("simiData_PostgresTableReadFromService")
@UiDescriptor("schema-reader-screen.xml")
@LookupComponent("tableShortInfosTable")
@LoadDataBeforeShow
public class SchemaReaderScreen extends Screen {
    private PostgresDB postgresDB;
    private String schema;
    private String table;
    private TableAndFieldInfo result;

    @Inject
    private CollectionLoader<PostgresDB> postgresDBsDl;
    @Inject
    private LookupField<PostgresDB> postgresDBLookupField;
    @Inject
    private Notifications notifications;
    @Inject
    private TextField<String> schemaField;
    @Inject
    private TextField<String> tableField;
    @Inject
    private SchemaReaderBean schemaReaderBean;
    @Inject
    private CollectionContainer<TableShortInfo> TableShortInfosDc;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Table<TableShortInfo> tableShortInfosTable;
    @Inject
    private Action selectAction;

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public PostgresDB getPostgresDB() {
        return postgresDB;
    }

    public void setPostgresDB(PostgresDB postgresDB) {
        this.postgresDB = postgresDB;
    }

    public TableAndFieldInfo getResult() {
        return result;
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        // set default postgresDB
        postgresDBsDl.getContainer().getItems().stream()
                .filter(postgresDB -> {
                    if (postgresDB.getDefaultValue() == null) {
                        return false;
                    } else {
                        return postgresDB.getDefaultValue();
                    }
                })
                .findFirst()
                .ifPresent(db -> postgresDBLookupField.setValue(db));

        // populate table, schema and postgresDB search fields
        tableField.setValue(table);
        schemaField.setValue(schema);
        if (postgresDB != null) {
            postgresDBLookupField.setValue(postgresDB);
        }

        selectAction.refreshState();
    }

    @Subscribe("searchBtn")
    public void onSearchBtnClick(Button.ClickEvent event) {
        try {
            PostgresDB postgresDB = postgresDBLookupField.getValue();
            TableListing tableListing = schemaReaderBean.getTableSearch(postgresDB, schemaField.getValue(), tableField.getValue());

            // assign search result to table and remember postgresDB
            TableShortInfosDc.setItems(tableListing.getTableViewList());
            setPostgresDB(postgresDB);

            if (tableListing.getTableViewList().isEmpty()) {
                notifications.create().withCaption("Keine Treffer").show();
            } else if (tableListing.getTruncatedTo() != null) {
                notifications.create().withCaption("Zu viele Treffer, die ersten " + tableListing.getTruncatedTo() + " werden angezeigt.").show();
            }
        } catch (IllegalArgumentException e) {
            notifications.create(Notifications.NotificationType.WARNING).withCaption(e.getLocalizedMessage()).show();
        } catch (RestClientException e) {
            dialogs.createExceptionDialog().withMessage(e.getMessage()).withThrowable(e).show();
        }
    }

    @Subscribe("tableShortInfosTable")
    public void onTableShortInfosTableSelection(Table.SelectionEvent<TableShortInfo> event) {
        selectAction.refreshState();
    }

    @Install(to = "tableShortInfosTable", subject = "lookupSelectHandler")
    private void tableShortInfosTableLookupSelectHandler(Collection<TableShortInfo> collection) {
        selectAction.actionPerform(tableShortInfosTable);
    }

    @Subscribe("cancelBtn")
    public void onCancelBtnClick(Button.ClickEvent event) {
        close(StandardOutcome.CLOSE);
    }

    @Install(to = "selectAction", subject = "enabledRule")
    private boolean selectActionEnabledRule() {
        return tableShortInfosTable.getSingleSelected() != null;
    }

    @Subscribe("selectAction")
    public void onSelectAction(Action.ActionPerformedEvent event) {
        TableShortInfo tableShortInfo = tableShortInfosTable.getSingleSelected();

        if (tableShortInfo != null) {
            try {
                result = schemaReaderBean.getTableInfo(getPostgresDB(), tableShortInfo);
                close(StandardOutcome.COMMIT);
            } catch (RestClientException e) {
                dialogs.createExceptionDialog().withMessage(e.getMessage()).withThrowable(e).show();
            }
        }
    }
}
