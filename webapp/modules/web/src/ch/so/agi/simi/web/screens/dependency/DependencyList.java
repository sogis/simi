package ch.so.agi.simi.web.screens.dependency;

import ch.so.agi.simi.core.dependency.*;
import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.dependency.DependencyBase;
import ch.so.agi.simi.entity.product.DataProduct;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ContentMode;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@UiController("simi_DependencyList")
@UiDescriptor("dependency-list.xml")
public class DependencyList extends StandardLookup<DataProduct> {

    private static final String ROOTOBJ_COL_NAME = "rootObjName";

    @Inject
    CollectionContainer<DependencyRootDto> rootObjDc;

    @Inject
    CollectionContainer<DependencyDto> dependenciesDc;

    @Inject
    DependencyRootService rootService;

    @Inject
    DependencyListService listService;

    @Inject
    DataManager dataManager;

    @Inject
    GroupTable<DependencyDto> dependenciesTable;

    @Inject
    Notifications notifications;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        List<DependencyRootDto> roots = rootService.queryRoots();
        roots.sort((root1, root2) -> root1.getDisplay().compareTo(root2.getDisplay()));

        rootObjDc.getMutableItems().addAll(roots);
    }

    @Subscribe("btnListDependencies")
    public void onBtnListDependenciesClick(Button.ClickEvent event) {

        DependencyRootDto root = rootObjDc.getItemOrNull();

        if(root == null)
            return;

        List<UUID> ids = List.of(root.getId());
        if(root.getIsSchema())
            ids = resolveTableIds(root.getId());

        DependencyListResult res = listService.listDependenciesForObjs(ids);

        dependenciesDc.setItems(res.getDependencies());

        if(res.getMessages().size() > 0){
            String allMsg = res.getMessages().stream().collect(Collectors.joining("\\n"));

            notifications.create(Notifications.NotificationType.TRAY)
                    .withContentMode(ContentMode.HTML)
                    .withDescription(allMsg)
                    .show();
        }

        deferTableGrouping(res.getDependencies());
    }

    private void deferTableGrouping(List<DependencyDto> list) {
        Map<String, DependencyDto> rootMap = list.stream().collect(Collectors.toMap(
                DependencyDto::getRootObjName,
                Function.identity(),
                (dep1, dep2) -> dep1
        ));

        if(rootMap.size() > 1){ //group
            dependenciesTable.addColumn(new Table.Column(list.get(0).getMetaClass().getPropertyPath(ROOTOBJ_COL_NAME)));
            Table.Column c = dependenciesTable.getColumn(ROOTOBJ_COL_NAME);
            c.setWidth(100);
            c.setCaption("Ursprung");

            dependenciesTable.groupByColumns(ROOTOBJ_COL_NAME);
            dependenciesTable.expandAll();
        }
        else{ //ungroup
            dependenciesTable.ungroup();
            Table.Column c = dependenciesTable.getColumn(ROOTOBJ_COL_NAME);
            dependenciesTable.removeColumn(c);
        }
    }

    private List<UUID> resolveTableIds(UUID schemaId) {
        List<PostgresTable> tables = dataManager.load(PostgresTable.class)
                .query("select e from simiData_PostgresTable e where e.dbSchema.id = :id")
                .parameter("id", schemaId)
                .list();

        return tables.stream().map(tbl -> tbl.getId()).collect(Collectors.toList());
    }

}