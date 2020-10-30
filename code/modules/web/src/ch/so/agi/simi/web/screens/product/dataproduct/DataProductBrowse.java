package ch.so.agi.simi.web.screens.product.dataproduct;

import ch.so.agi.simi.entity.data.raster.RasterDS;
import ch.so.agi.simi.entity.data.raster.RasterView;
import ch.so.agi.simi.entity.data.tabular.PostgresTable;
import ch.so.agi.simi.entity.data.tabular.TableView;
import ch.so.agi.simi.entity.product.DataProduct;
import ch.so.agi.simi.entity.product.FacadeLayer;
import ch.so.agi.simi.entity.product.LayerGroup;
import ch.so.agi.simi.entity.product.Map;
import ch.so.agi.simi.web.CopyDataProductBean;
import ch.so.agi.simi.web.screens.FilterFragment;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.function.Consumer;

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
    private Notifications notifications;
    @Inject
    private CollectionLoader<DataProduct> dataProductsDl;
    @Inject
    private CopyDataProductBean copyDataProductBean;
    @Inject
    private FilterFragment filter;

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
        showLookupForEntity(PostgresTable.class, postgresTable -> {
            TableView tableView = metadata.create(TableView.class);
            tableView.setPostgresTable(postgresTable);
            showCreateEditorForDataProduct(tableView);
        });
    }

    @Subscribe("createBtn.createRasterView")
    protected void onCreateBtnCreateRasterView(Action.ActionPerformedEvent event) {
        showLookupForEntity(RasterDS.class, rasterDS -> {
            RasterView rasterView = metadata.create(RasterView.class);
            rasterView.setRasterDS(rasterDS);
            showCreateEditorForDataProduct(rasterView);
        });
    }

    private <T, E extends Entity<T>> void showLookupForEntity(Class<E> entityClass, Consumer<E> selectHandler) {
        screenBuilders.lookup(entityClass, this)
                .withOpenMode(OpenMode.DIALOG)
                .withSelectHandler(rasterDSs -> selectHandler.accept(rasterDSs.iterator().next()))
                .build()
                .show();
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
            try {
                copyDataProductBean.copyDataProduct(selectedItem.getClass(), selectedItem.getId());

                filter.setFilter(selectedItem.getIdentifier());

                // reload dataContainer
                dataProductsDl.load();
            } catch (IllegalArgumentException e) {
                notifications.create(Notifications.NotificationType.WARNING).withCaption(e.getLocalizedMessage()).show();
            }
        }
    }

    @Install(to = "dataProductsTable.edit", subject = "afterCommitHandler")
    private void dataProductsTableEditAfterCommitHandler(DataProduct dataProduct) {
        // reload dataContainer after entity was edited
        dataProductsDl.load();
    }
}