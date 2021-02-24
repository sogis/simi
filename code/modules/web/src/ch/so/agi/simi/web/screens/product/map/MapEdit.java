package ch.so.agi.simi.web.screens.product.map;

import ch.so.agi.simi.entity.product.non_dsv.Map;
import ch.so.agi.simi.entity.product.non_dsv.LayerGroup;
import ch.so.agi.simi.entity.product.non_dsv.PropertiesInList;
import ch.so.agi.simi.web.beans.sort.SortBean;
import ch.so.agi.simi.web.beans.copy.UpdateFromOtherListsBean;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.*;

@UiController("simiProduct_Map.edit")
@UiDescriptor("map-edit.xml")
@EditedEntityContainer("dataProductDc")
@LoadDataBeforeShow
public class MapEdit extends StandardEditor<Map> {

    @Inject
    private CollectionPropertyContainer<PropertiesInList> propertiesInListDc;
    @Inject
    private Table<PropertiesInList> propertiesInListTable;
    @Inject
    private SortBean sortBean;
    @Inject
    private DataContext dataContext;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private UpdateFromOtherListsBean listUpdateBean;


    @Subscribe("btnPilAddSingleActor")
    public void onBtnPilAddSingleActorClick(Button.ClickEvent event) {
        PropertiesInList pil = dataContext.create(PropertiesInList.class);
        pil.setProductList(this.getEditedEntity());

        propertiesInListDc.getMutableItems().add(pil);

        propertiesInListTable.requestFocus(pil, "singleActor");
    }

    @Subscribe("btnPilSortAction")
    public void onBtnPilSortActionClick(Button.ClickEvent event) {
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