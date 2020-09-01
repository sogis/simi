package ch.so.agi.simi.web.screens.data.tabular.tableview;

import ch.so.agi.simi.entity.DataProduct_PubScope;
import ch.so.agi.simi.entity.product.Map;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.tabular.TableView;

import javax.inject.Inject;

@UiController("simi_TableView.edit")
@UiDescriptor("table-view-edit.xml")
@EditedEntityContainer("tableViewDc")
@LoadDataBeforeShow
public class TableViewEdit extends StandardEditor<TableView> {
    @Inject
    private CollectionLoader<DataProduct_PubScope> pubScopesDl;

    @Subscribe
    public void onInitEntity(InitEntityEvent<TableView> event) {
        TableView tableView = event.getEntity();
        pubScopesDl.load();
        pubScopesDl.getContainer().getItems().stream()
                .filter(DataProduct_PubScope::getDefaultValue)
                .findFirst()
                .ifPresent(tableView::setPubScope);
    }
}