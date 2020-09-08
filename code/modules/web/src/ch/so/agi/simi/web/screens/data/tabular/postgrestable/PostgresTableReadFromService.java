package ch.so.agi.simi.web.screens.data.tabular.postgrestable;

import ch.so.agi.simi.entity.data.tabular.PostgresDB;
import ch.so.agi.simi.entity.data.tabular.PostgresTableFromService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.List;

@UiController("simiData_PostgresTableReadFromService")
@UiDescriptor("postgres-table-read-from-service.xml")
@LookupComponent("postgresTablesFromServiceTable")
@LoadDataBeforeShow
public class PostgresTableReadFromService extends StandardLookup<PostgresTableFromService> {
    @Inject
    private CollectionLoader<PostgresDB> postgresDBsDl;
    @Inject
    private LookupField<PostgresDB> postgresDBLookupField;
    @Inject
    private CollectionContainer<PostgresTableFromService> postgresTablesFromServiceDc;
    @Inject
    private Notifications notifications;
    @Inject
    private TextField<String> schemaField;
    @Inject
    private TextField<String> tableField;
    @Inject
    private DataManager dataManager;

    private String schema;

    private String table;

    private PostgresDB postgresDB;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTable() {
        return table;
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

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
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

        tableField.setValue(table);

        schemaField.setValue(schema);

        if (postgresDB != null) {
            postgresDBLookupField.setValue(postgresDB);
        }
    }

    @Subscribe("searchBtn")
    public void onSearchBtnClick(Button.ClickEvent event) {
        notifications.create().withCaption("Searching for " + postgresDBLookupField.getValue().getDbName() + "." + schemaField.getValue() + "." + tableField.getValue()).show();

        PostgresTableFromService table1 = dataManager.create(PostgresTableFromService.class);
        table1.setSchema("Rooftop");
        table1.setTable("Cloud");

        PostgresTableFromService table2 = dataManager.create(PostgresTableFromService.class);
        table2.setSchema("River");
        table2.setTable("Dolphin");

        postgresTablesFromServiceDc.setItems(List.of(table1, table2));
    }
}
