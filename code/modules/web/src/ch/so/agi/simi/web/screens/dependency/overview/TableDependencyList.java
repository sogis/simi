package ch.so.agi.simi.web.screens.dependency.overview;

import ch.so.agi.simi.service.dependency.DependencyInfo;
import ch.so.agi.simi.service.dependency.DependencyService;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.datasetview.TableView;

import javax.inject.Inject;
import java.util.List;

@UiController("simiData_TableDependency.list")
@UiDescriptor("pgtable-dependency-list.xml")
@LookupComponent("tableViewsTable")
@LoadDataBeforeShow
public class TableDependencyList extends StandardLookup<TableView> {

    @Inject
    private DependencyService bean;

    @Inject
    private TextField fldTableName;

    @Inject
    private Notifications notifications;

    @Inject
    private CollectionContainer<DependencyInfo> dependenciesDc;

    @Subscribe("fldTableName")
    public void onFldTableNameEnterPress(TextInputField.EnterPressEvent event) {
        queryNonRegisteredTable();
    }

    @Subscribe("btnTableName")
    public void onBtnTableNameClick(Button.ClickEvent event) {
        queryNonRegisteredTable();
    }

    private void queryNonRegisteredTable(){
        if(fldTableName.getValue() == null)
            return;

        String[] parts = fldTableName.getValue().toString().split("/");
        if(parts == null || parts.length != 2)
            return;

        List<DependencyInfo> dependencies = bean.collectDependenciesForUnregisteredTable(parts[0], parts[1]);

        int numFound = 0;
        if(dependencies != null)
            numFound = dependencies.size();

        dependenciesDc.setItems(dependencies);
    }
}