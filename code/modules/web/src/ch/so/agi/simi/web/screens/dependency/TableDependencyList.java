package ch.so.agi.simi.web.screens.dependency;

import ch.so.agi.simi.core.dependency.DependencyInfo;
import ch.so.agi.simi.core.dependency.DependencyService;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.KeyValueContainer;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.datasetview.TableView;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@UiController("simiData_TableDependency.list")
@UiDescriptor("pgtable-dependency-list.xml")
@LookupComponent("tableViewsTable")
@LoadDataBeforeShow
public class TableDependencyList extends StandardLookup<TableView> {

    @Inject
    private DependencyService bean;

    @Inject
    private KeyValueContainer tablesDc;

    @Inject
    private CollectionContainer<DependencyInfo> dependenciesDc;

    @Subscribe("btnListDependencies")
    public void onBtnListDependenciesClick(Button.ClickEvent event) {
        KeyValueEntity kv = tablesDc.getItemOrNull();

        if(kv == null)
            return;

        UUID tableUuid = kv.getValue("id");
        List<DependencyInfo> dependencies = bean.collectAllDependenciesForTable(tableUuid);

        int numFound = 0;
        if(dependencies != null)
            numFound = dependencies.size();

        dependenciesDc.setItems(dependencies);
    }

    @Subscribe(id = "tablesDc", target = Target.DATA_CONTAINER)
    public void onTablesDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<KeyValueEntity> event) {

        
    }
/*
    @Subscribe(id = "orderDc", target = Target.DATA_CONTAINER)
    protected void onOrderDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<Order> event) {
        Object str = event.getValue() instanceof Entity
                ? metadataTools.getInstanceName((Entity) event.getValue())
                : event.getValue();
        notifications.create()
                .withCaption(event.getProperty() + " = " + str)
                .show();
    }*/
    
    
    
    /*

    @Subscribe("fldTableName")
    public void onFldTableNameEnterPress(TextInputField.EnterPressEvent event) {
        queryNonRegisteredTable();
    }

    @Subscribe("btnTableName")
    public void onBtnTableNameClick(Button.ClickEvent event) {
        queryNonRegisteredTable();
    }
    
    /*

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
    
     */
}