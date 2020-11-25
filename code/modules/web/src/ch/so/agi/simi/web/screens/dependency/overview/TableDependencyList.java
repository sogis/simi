package ch.so.agi.simi.web.screens.dependency.overview;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.tabular.TableView;

@UiController("simiData_TableDependency.list")
@UiDescriptor("pgtable-dependency-list.xml")
@LookupComponent("tableViewsTable")
@LoadDataBeforeShow
public class TableDependencyList extends StandardLookup<TableView> {
}