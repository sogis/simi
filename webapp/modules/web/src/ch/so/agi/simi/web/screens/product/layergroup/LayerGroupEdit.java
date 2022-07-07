package ch.so.agi.simi.web.screens.product.layergroup;

import ch.so.agi.simi.entity.data.datasetview.DataSetView;
import ch.so.agi.simi.entity.product.FacadeLayer;
import ch.so.agi.simi.entity.product.LayerGroup;
import ch.so.agi.simi.entity.product.PropertiesInFacade;
import ch.so.agi.simi.entity.product.PropertiesInList;
import ch.so.agi.simi.web.beans.sort.SortBean;
import ch.so.agi.simi.web.beans.copy.UpdateFromOtherListsBean;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
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
    @Inject
    DataManager dataManager;

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        this.getEditedEntity().updateDerivedIdentifier(dataManager);
    }

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