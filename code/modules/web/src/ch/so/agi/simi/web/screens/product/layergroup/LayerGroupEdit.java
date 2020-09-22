package ch.so.agi.simi.web.screens.product.layergroup;

import ch.so.agi.simi.entity.product.*;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Notifications;
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

@UiController("simiProduct_LayerGroup.edit")
@UiDescriptor("layer-group-edit.xml")
@EditedEntityContainer("dataProductDc")
@LoadDataBeforeShow
public class LayerGroupEdit extends StandardEditor<LayerGroup> {
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Metadata metadata;
    @Inject
    private CollectionPropertyContainer<PropertiesInList> singleActorsDc;
    @Inject
    private Table<PropertiesInList> singleActorsTable;
    @Inject
    private InstanceContainer<LayerGroup> dataProductDc;
    @Inject
    private DataContext dataContext;
    @Inject
    private Notifications notifications;

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
        propertiesInList.setProductList(dataProductDc.getItem());
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

    @Subscribe("convertToFacadeLayer")
    public void onConvertToFacadeLayer(Action.ActionPerformedEvent event) {
        // check if conversion is possible
        LayerGroup layerGroup = this.getEditedEntity();
        if (layerGroup.getSingleActors() != null && layerGroup.getSingleActors().stream().anyMatch(pil -> !(pil.getSingleActor() instanceof DataSetView))) {
            notifications.create().withCaption("LayerGroup kann nicht in ein FacadeLayer umgewandelt werden, weil nicht alle Layer in DataSetViews umgewandelt werden können.").show();
            return;
        }

        // change identifier of old LG to prevent UK constraint error on identifier
        String identifier = this.getEditedEntity().getIdentifier();
        this.getEditedEntity().setIdentifier(identifier + "_toFL");
        try {
            dataContext.commit();
        } catch (Exception e) {
            this.getEditedEntity().setIdentifier(identifier);
            throw e;
        }

        layerGroup = this.getEditedEntity();
        dataContext.remove(layerGroup);

        FacadeLayer facadeLayer = dataContext.create(FacadeLayer.class);
        facadeLayer.setIdentifier(identifier);
        facadeLayer.setPubScope(layerGroup.getPubScope());
        facadeLayer.setKeywords(layerGroup.getKeywords());
        facadeLayer.setRemarks(layerGroup.getRemarks());
        facadeLayer.setSynonyms(layerGroup.getSynonyms());
        facadeLayer.setTitle(layerGroup.getTitle());
        facadeLayer.setReleasedAt(layerGroup.getReleasedAt());
        facadeLayer.setReleasedThrough(layerGroup.getReleasedThrough());

        if (layerGroup.getSingleActors() != null) {
            for (PropertiesInList propertiesInList : layerGroup.getSingleActors()) {
                PropertiesInFacade propertiesInFacade = dataContext.create(PropertiesInFacade.class);
                dataContext.remove(propertiesInList);

                propertiesInFacade.setSort(propertiesInList.getSort());
                propertiesInFacade.setTransparency(propertiesInList.getTransparency());
                propertiesInFacade.setFacadeLayer(facadeLayer);
                propertiesInFacade.setDataSetView((DataSetView) propertiesInList.getSingleActor());
            }
        }

        OperationResult result = closeWithCommit();

        if (result == OperationResult.success()) {
            screenBuilders.editor(FacadeLayer.class, this)
                    .editEntity(facadeLayer)
                    .build()
                    .show();
        } else {
            // change identifier back to original value
            layerGroup.setIdentifier(identifier);
            dataContext.commit();
        }
    }
}