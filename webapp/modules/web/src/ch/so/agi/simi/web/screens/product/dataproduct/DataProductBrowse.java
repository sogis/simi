package ch.so.agi.simi.web.screens.product.dataproduct;

import ch.so.agi.simi.core.copy.CopyService;
import ch.so.agi.simi.core.morph.MorphProductService;
import ch.so.agi.simi.core.morph.ProdType;
import ch.so.agi.simi.core.props.PropertiesCheckerService;
import ch.so.agi.simi.entity.data.datasetview.RasterView;
import ch.so.agi.simi.entity.data.datasetview.TableView;
import ch.so.agi.simi.entity.product.*;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("simiProduct_DataProduct.browse")
@UiDescriptor("data-product-browse.xml")
@LookupComponent("dataProductsTable")
@LoadDataBeforeShow
public class DataProductBrowse extends StandardLookup<DataProduct> {

    @Inject
    private PropertiesCheckerService service;

    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Table<DataProduct> dataProductsTable;
    @Inject
    private Metadata metadata;
    @Inject
    private CollectionLoader<DataProduct> dataProductsDl;
    @Inject
    private CopyService copyService;
    @Inject
    private MorphProductService morphService;


    @Subscribe("createBtn.createMap")
    protected void onCreateBtnCreateMap(Action.ActionPerformedEvent event) {
        Map map = metadata.create(Map.class);
        showCreateEditorForDataProduct(map);
    }

    @Subscribe("createBtn.createLayerGroup")
    protected void onCreateBtnCreateLayerGroup(Action.ActionPerformedEvent event) {
        LayerGroup layerGroup = metadata.create(LayerGroup.class);
        showCreateEditorForDataProduct(layerGroup);
    }

    @Subscribe("createBtn.createFacadeLayer")
    protected void onCreateBtnCreateFacadeLayer(Action.ActionPerformedEvent event) {
        FacadeLayer facadeLayer = metadata.create(FacadeLayer.class);
        showCreateEditorForDataProduct(facadeLayer);
    }

    @Subscribe("createBtn.createTableView")
    protected void onCreateBtnCreateTableView(Action.ActionPerformedEvent event) {
        TableView tableView = metadata.create(TableView.class);
        showCreateEditorForDataProduct(tableView);
    }

    @Subscribe("createBtn.createRasterView")
    protected void onCreateBtnCreateRasterView(Action.ActionPerformedEvent event) {
        RasterView rv = metadata.create(RasterView.class);
        showCreateEditorForDataProduct(rv);
    }

    @Subscribe("createBtn.createExtLayer")
    protected void onCreateBtnCreateExtLayer(Action.ActionPerformedEvent event) {
        ExternalWmsLayer rv = metadata.create(ExternalWmsLayer.class);
        showCreateEditorForDataProduct(rv);
    }

    private void showCreateEditorForDataProduct(DataProduct dataProduct) {
        screenBuilders.editor(dataProductsTable)
                .editEntity(dataProduct)
                .build()
                .show();
    }

    @Subscribe("dataProductsTable.copy")
    public void onDataProductsTableCopy(Action.ActionPerformedEvent event) {
        DataProduct selectedItem = dataProductsTable.getSingleSelected();

        if (selectedItem != null) {
            copyService.copyProduct(selectedItem.getId());

            dataProductsDl.load();
        }
    }

    @Install(to = "dataProductsTable.edit", subject = "afterCommitHandler")
    private void dataProductsTableEditAfterCommitHandler(DataProduct dataProduct) {
        // reload dataContainer after entity was edited
        dataProductsDl.load();
    }


    @Subscribe("morphBtn.morphToFacadeLayer")
    protected void onMorphBtnMorphToFacadeLayer(Action.ActionPerformedEvent event) {
        morph(ProdType.FACADELAYER);
    }

    @Subscribe("morphBtn.morphToLayerGroup")
    protected void onMorphBtnMorphToLayerGroup(Action.ActionPerformedEvent event) {
        morph(ProdType.LAYERGROUP);
    }

    @Subscribe("morphBtn.morphToMap")
    protected void onMorphBtnMorphToMap(Action.ActionPerformedEvent event) {
        morph(ProdType.MAP);
    }

    private void morph(ProdType toType){
        DataProduct selectedItem = dataProductsTable.getSingleSelected();

        if (selectedItem == null)
            return;

        ProdType fromType = null;
        if(selectedItem instanceof FacadeLayer)
            fromType = ProdType.FACADELAYER;
        else if (selectedItem instanceof LayerGroup)
            fromType= ProdType.LAYERGROUP;
        else if (selectedItem instanceof Map)
            fromType= ProdType.MAP;

        morphService.morphProduct(selectedItem.getId(), fromType, toType);

        dataProductsDl.load();
    }
}