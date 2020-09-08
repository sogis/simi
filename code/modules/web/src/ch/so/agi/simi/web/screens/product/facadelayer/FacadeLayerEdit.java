package ch.so.agi.simi.web.screens.product.facadelayer;

import ch.so.agi.simi.entity.DataProduct_PubScope;
import ch.so.agi.simi.entity.product.*;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@UiController("simi_FacadeLayer.edit")
@UiDescriptor("facade-layer-edit.xml")
@EditedEntityContainer("facadeLayerDc")
@LoadDataBeforeShow
public class FacadeLayerEdit extends StandardEditor<FacadeLayer> {
    @Inject
    private CollectionLoader<DataProduct_PubScope> pubScopesDl;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Metadata metadata;
    @Inject
    private CollectionPropertyContainer<PropertiesInFacade> dataSetViewsDc;
    @Inject
    private Table<PropertiesInFacade> dataSetViewsTable;
    @Inject
    private InstanceContainer<FacadeLayer> facadeLayerDc;

    @Subscribe
    public void onInitEntity(InitEntityEvent<FacadeLayer> event) {
        FacadeLayer facadeLayer = event.getEntity();
        pubScopesDl.load();
        pubScopesDl.getContainer().getItems().stream()
                .filter(DataProduct_PubScope::getDefaultValue)
                .findFirst()
                .ifPresent(facadeLayer::setPubScope);
    }

    @Subscribe("dataSetViewsTable.addDataSetView")
    public void onSingleActorsTableAddSingleActor(Action.ActionPerformedEvent event) {
        screenBuilders.lookup(DataSetView.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .withSelectHandler(dataSetViews -> {
                    dataSetViews.stream()
                            .map(this::createPropertiesInFacadeFromDataSetView)
                            .forEach(this::addToPropertiesInFacade);
                })
                .build()
                .show();
    }

    private PropertiesInFacade createPropertiesInFacadeFromDataSetView(DataSetView dataSetView) {
        PropertiesInFacade propertiesInFacade = metadata.create(PropertiesInFacade.class);
        propertiesInFacade.setFacadeLayer(facadeLayerDc.getItem());
        propertiesInFacade.setDataSetView(dataSetView);

        return propertiesInFacade;
    }

    private void addToPropertiesInFacade(PropertiesInFacade propertiesInFacade) {
        dataSetViewsDc.getMutableItems().add(propertiesInFacade);
    }

    @Subscribe("dataSetViewsTable.sortAction")
    public void onSingleActorsTableSortAction(Action.ActionPerformedEvent event) {
        dataSetViewsTable.sort("sort", Table.SortDirection.ASCENDING);
    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPreCommit(DataContext.PreCommitEvent event) {
        int i = 0;
        List<PropertiesInFacade> singleActors = dataSetViewsDc.getItems().stream()
                .sorted(Comparator.comparing(ChildLayerProperties::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        // go through the data container items. The same can be done using getEditedEntity().getSingleActorList().
        for (PropertiesInFacade item : singleActors) {
            // set new value and add modified instance to the commit list
            item.setSort(i);
            event.getModifiedInstances().add(item);
            i += 10;
        }
    }
}