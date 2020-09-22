package ch.so.agi.simi.web.screens.product.facadelayer;

import ch.so.agi.simi.entity.product.*;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.util.OperationResult;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@UiController("simiProduct_FacadeLayer.edit")
@UiDescriptor("facade-layer-edit.xml")
@EditedEntityContainer("dataProductDc")
@LoadDataBeforeShow
public class FacadeLayerEdit extends StandardEditor<FacadeLayer> {
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Metadata metadata;
    @Inject
    private CollectionPropertyContainer<PropertiesInFacade> dataSetViewsDc;
    @Inject
    private Table<PropertiesInFacade> dataSetViewsTable;
    @Inject
    private InstanceContainer<FacadeLayer> dataProductDc;
    @Inject
    private DataContext dataContext;
    @Inject
    private MetadataTools metadataTools;

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
        propertiesInFacade.setFacadeLayer(dataProductDc.getItem());
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

    @Subscribe("convertToLayerGroup")
    public void onConvertToLayerGroup(Action.ActionPerformedEvent event) {
        // change identifier of old FL to prevent UK constraint error on identifier
        String identifier = this.getEditedEntity().getIdentifier();
        this.getEditedEntity().setIdentifier(identifier + "_toLG");
        try {
            dataContext.commit();
        } catch (Exception e) {
            this.getEditedEntity().setIdentifier(identifier);
            throw e;
        }

        FacadeLayer facadeLayer = this.getEditedEntity();
        dataContext.remove(facadeLayer);

        LayerGroup layerGroup = dataContext.create(LayerGroup.class);
        layerGroup.setIdentifier(identifier);
        layerGroup.setPubScope(facadeLayer.getPubScope());
        layerGroup.setKeywords(facadeLayer.getKeywords());
        layerGroup.setRemarks(facadeLayer.getRemarks());
        layerGroup.setSynonyms(facadeLayer.getSynonyms());
        layerGroup.setTitle(facadeLayer.getTitle());
        layerGroup.setReleasedAt(facadeLayer.getReleasedAt());
        layerGroup.setReleasedThrough(facadeLayer.getReleasedThrough());

        if (facadeLayer.getDataSetViews() != null) {
            for (PropertiesInFacade propertiesInFacade : facadeLayer.getDataSetViews()) {
                PropertiesInList propertiesInList = dataContext.create(PropertiesInList.class);
                dataContext.remove(propertiesInFacade);

                propertiesInList.setSort(propertiesInFacade.getSort());
                propertiesInList.setTransparency(propertiesInFacade.getTransparency());
                propertiesInList.setProductList(layerGroup);
                propertiesInList.setSingleActor(propertiesInFacade.getDataSetView());
            }
        }

        OperationResult result = closeWithCommit();

        if (result == OperationResult.success()) {
            screenBuilders.editor(LayerGroup.class, this)
                    .editEntity(layerGroup)
                    .build()
                    .show();
        } else {
            // change identifier back to original value
            facadeLayer.setIdentifier(identifier);
            dataContext.commit();
        }
    }
}