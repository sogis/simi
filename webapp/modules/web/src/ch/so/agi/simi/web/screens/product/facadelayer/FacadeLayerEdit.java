package ch.so.agi.simi.web.screens.product.facadelayer;

import ch.so.agi.simi.entity.product.FacadeLayer;
import ch.so.agi.simi.entity.product.LayerGroup;
import ch.so.agi.simi.entity.product.PropertiesInFacade;
import ch.so.agi.simi.entity.product.PropertiesInList;
import ch.so.agi.simi.web.beans.sort.SortBean;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.util.OperationResult;

import javax.inject.Inject;
import java.util.List;

@UiController("simiProduct_FacadeLayer.edit")
@UiDescriptor("facade-layer-edit.xml")
@EditedEntityContainer("dataProductDc")
@LoadDataBeforeShow
public class FacadeLayerEdit extends StandardEditor<FacadeLayer> {

    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private CollectionPropertyContainer<PropertiesInFacade> propertiesInFacadeDc;
    @Inject
    private Table<PropertiesInFacade> propertiesInFacadeTable;
    @Inject
    private DataContext dataContext;
    @Inject
    private DataManager manager;
    @Inject
    private SortBean sortBean;

    @Subscribe("btnPifAddDataSetView")
    public void onBtnPifAddDataSetViewClick(Button.ClickEvent event) {
        PropertiesInFacade pif = dataContext.create(PropertiesInFacade.class);
        pif.setFacadeLayer(this.getEditedEntity());

        propertiesInFacadeDc.getMutableItems().add(pif);

        propertiesInFacadeTable.requestFocus(pif, "dataSetView");
    }

    @Subscribe("btnPifSort")
    public void onBtnPifSortClick(Button.ClickEvent event) {
        propertiesInFacadeTable.sort("sort", Table.SortDirection.ASCENDING);
    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPreCommit(DataContext.PreCommitEvent event) {
        List<PropertiesInFacade> entities = sortBean.AdjustSort(propertiesInFacadeDc.getItems());

        //add modified instances to the commit list
        event.getModifiedInstances().addAll(entities);
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        this.getEditedEntity().updateDerivedIdentifier(manager);
    }
}