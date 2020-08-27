package ch.so.agi.simi.web.screens.product.map;

import ch.so.agi.simi.entity.DataProduct_PubScope;
import ch.so.agi.simi.entity.product.ChildLayerProperties;
import ch.so.agi.simi.entity.product.PropertiesInList;
import ch.so.agi.simi.entity.product.SingleActor;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.Map;
import org.apache.commons.lang3.NotImplementedException;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@UiController("simi_Map.edit")
@UiDescriptor("map-edit.xml")
@EditedEntityContainer("mapDc")
@LoadDataBeforeShow
public class MapEdit extends StandardEditor<Map> {
    @Inject
    private CollectionLoader<DataProduct_PubScope> pubScopesDl;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Metadata metadata;
    @Inject
    private InstanceContainer<Map> mapDc;
    @Inject
    private CollectionPropertyContainer<PropertiesInList> singleActorsDc;
    @Inject
    private Table<PropertiesInList> singleActorsTable;

    @Subscribe
    public void onInitEntity(InitEntityEvent<Map> event) {
        Map map = event.getEntity();
        pubScopesDl.load();
        pubScopesDl.getContainer().getItems().stream()
                .filter(DataProduct_PubScope::getDefaultValue)
                .findFirst()
                .ifPresent(map::setPubScope);
    }

    @Subscribe("singleActorsTable.addSingleActor")
    public void onSingleActorsTableAddSingleActor(Action.ActionPerformedEvent event) {
        screenBuilders.lookup(SingleActor.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .withSelectHandler(singleActors -> {
                    singleActors.stream()
                            .map(this::createPropertiesInListFromSingleActor)
                            .forEach(this::addToPropertiesInList);
                })
                .build()
                .show();
    }

    private PropertiesInList createPropertiesInListFromSingleActor(SingleActor singleActor) {
        PropertiesInList propertiesInList = metadata.create(PropertiesInList.class);
        propertiesInList.setProductList(mapDc.getItem());
        propertiesInList.setSingleActor(singleActor);

        return propertiesInList;
    }

    private void addToPropertiesInList(PropertiesInList propertiesInList) {
        singleActorsDc.getMutableItems().add(propertiesInList);
    }

    @Subscribe("singleActorsTable.sortAction")
    public void onSingleActorsTableSortAction(Action.ActionPerformedEvent event) {
        singleActorsTable.sort("sort", Table.SortDirection.ASCENDING);
    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPreCommit(DataContext.PreCommitEvent event) {
        int i = 0;
        List<PropertiesInList> singleActors = singleActorsDc.getItems().stream()
                .sorted(Comparator.comparing(ChildLayerProperties::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        // go through the data container items. The same can be done using getEditedEntity().getSingleActorList().
        for (PropertiesInList item : singleActors) {
            // set new value and add modified instance to the commit list
            item.setSort(i);
            event.getModifiedInstances().add(item);
            i += 10;
        }
    }

    @Subscribe("singleActorsTable.addSingleActorsFromLayerGroup")
    public void onSingleActorsTableAddSingleActorsFromLayerGroup(Action.ActionPerformedEvent event) {
        throw new NotImplementedException("onSingleActorsTableAddSingleActorsFromLayerGroup");
    }
}