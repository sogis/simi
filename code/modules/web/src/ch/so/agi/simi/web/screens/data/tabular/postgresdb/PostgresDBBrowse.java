package ch.so.agi.simi.web.screens.data.tabular.postgresdb;

import ch.so.agi.simi.entity.data.tabular.TableView;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.tabular.PostgresDB;

import javax.inject.Inject;

@UiController("simi_PostgresDB.browse")
@UiDescriptor("postgres-db-browse.xml")
@LookupComponent("postgresDBsTable")
@LoadDataBeforeShow
public class PostgresDBBrowse extends StandardLookup<PostgresDB> {
}