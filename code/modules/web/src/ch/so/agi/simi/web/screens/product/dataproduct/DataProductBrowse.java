package ch.so.agi.simi.web.screens.product.dataproduct;

import ch.so.agi.simi.core.copy.CopyService;
import ch.so.agi.simi.entity.data.datasetview.RasterView;
import ch.so.agi.simi.entity.data.datasetview.TableView;
import ch.so.agi.simi.entity.product.non_dsv.DataProduct;
import ch.so.agi.simi.entity.product.non_dsv.FacadeLayer;
import ch.so.agi.simi.entity.product.non_dsv.LayerGroup;
import ch.so.agi.simi.entity.product.non_dsv.Map;
import ch.so.agi.simi.web.beans.publish.JobRunner;
import ch.so.agi.simi.web.beans.publish.PublishConfig;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.WebBrowserTools;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
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
    private JobRunner runner;
    @Inject
    PublishConfig config;
    @Inject
    private WebBrowserTools webBrowserTools;

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

    @Subscribe("btnPublish")
    public void onBtnPublishClick(Button.ClickEvent event) {
        String jenkinsPageUrl = null;

        boolean inTime = runner.runJob(config);
        if(inTime)
            jenkinsPageUrl = config.getJobBaseUrl() + "/lastBuild";
        else
            jenkinsPageUrl = config.getJobBaseUrl();

        webBrowserTools.showWebPage(jenkinsPageUrl, ParamsMap.of("_target", "blank"));
    }
}