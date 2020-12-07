package ch.so.agi.simi.web.screens.product.layergroup;

import ch.so.agi.simi.entity.product.*;
import ch.so.agi.simi.web.SortBean;
import ch.so.agi.simi.web.beans.copy.UpdateFromOtherListsBean;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.util.OperationResult;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@UiController("simiProduct_LayerGroup.edit")
@UiDescriptor("layer-group-edit.xml")
@EditedEntityContainer("dataProductDc")
@LoadDataBeforeShow
public class LayerGroupEdit extends StandardEditor<LayerGroup> {
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private CollectionPropertyContainer<PropertiesInList> propertiesInListDc;
    @Inject
    private Table<PropertiesInList> propertiesInListTable;
    @Inject
    private DataContext dataContext;
    @Inject
    private Notifications notifications;
    @Inject
    private SortBean sortBean;
    @Inject
    private UpdateFromOtherListsBean listUpdateBean;

    @Subscribe("btnPilAdd")
    public void onBtnPilAddClick(Button.ClickEvent event) {
        PropertiesInList pil = dataContext.create(PropertiesInList.class);
        pil.setProductList(this.getEditedEntity());

        propertiesInListDc.getMutableItems().add(pil);

        propertiesInListTable.requestFocus(pil, "singleActor");
    }

    @Subscribe("btnPilSort")
    public void onBtnPilSortClick(Button.ClickEvent event) {
        propertiesInListTable.sort("sort", Table.SortDirection.ASCENDING);
    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPreCommit(DataContext.PreCommitEvent event) {
        List<PropertiesInList> entities = sortBean.AdjustSort(propertiesInListDc.getItems());

        //add modified instances to the commit list
        event.getModifiedInstances().addAll(entities);
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

    @Subscribe("btnPilAddSingleActorsFromLayerGroup")
    public void onBtnPilAddSingleActorsFromLayerGroupClick(Button.ClickEvent event) {
        //throw new NotImplementedException("onpropertiesInListTableAddSingleActorsFromLayerGroup");

        screenBuilders.lookup(LayerGroup.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .withSelectHandler(layerGroups -> {
                    groupsSelected(layerGroups.iterator());
                })
                .build()
                .show();
    }

    private void groupsSelected(Iterator<LayerGroup> selectedGroupsIterator){

        Optional<PropertiesInList> firstAdded = listUpdateBean.updateLayersFromOtherLists(
                this.getEditedEntity().getId(),
                propertiesInListDc.getMutableItems(),
                selectedGroupsIterator
        );

        if(!firstAdded.isPresent())
            return;

        propertiesInListTable.requestFocus(firstAdded.get(), "sort");
    }
}