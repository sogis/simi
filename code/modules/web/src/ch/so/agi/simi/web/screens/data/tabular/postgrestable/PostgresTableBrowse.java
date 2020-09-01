package ch.so.agi.simi.web.screens.data.tabular.postgrestable;

import ch.so.agi.simi.entity.data.tabular.TableView;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.tabular.PostgresTable;

import javax.inject.Inject;

@UiController("simi_PostgresTable.browse")
@UiDescriptor("postgres-table-browse.xml")
@LookupComponent("postgresTablesTable")
@LoadDataBeforeShow
public class PostgresTableBrowse extends StandardLookup<PostgresTable> {
    @Inject
    private ScreenBuilders screenBuilders;

    @Subscribe("createTableViewBtn")
    public void onCreateTableViewBtnClick(Button.ClickEvent event) {
        screenBuilders.editor(TableView.class, this)
                .newEntity()
                .build()
                .show();
    }
}