package ch.so.agi.simi.web.screens.product.map;

import ch.so.agi.simi.entity.product.Map;
import ch.so.agi.simi.entity.product.PropertiesInList;
import ch.so.agi.simi.entity.product.SingleActor;
import ch.so.agi.simi.web.SortBean;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import org.apache.commons.lang3.NotImplementedException;

import javax.inject.Inject;
import java.util.List;

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
        throw new NotImplementedException("onpropertiesInListTableAddSingleActorsFromLayerGroup");
    }
}