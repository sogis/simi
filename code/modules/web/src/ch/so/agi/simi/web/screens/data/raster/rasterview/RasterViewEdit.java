package ch.so.agi.simi.web.screens.data.raster.rasterview;

import ch.so.agi.simi.entity.DataProduct_PubScope;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.raster.RasterView;

import javax.inject.Inject;

@UiController("simiData_RasterView.edit")
@UiDescriptor("raster-view-edit.xml")
@EditedEntityContainer("rasterViewDc")
@LoadDataBeforeShow
public class RasterViewEdit extends StandardEditor<RasterView> {
    @Inject
    private CollectionLoader<DataProduct_PubScope> pubScopesDl;

    @Subscribe
    public void onInitEntity(InitEntityEvent<RasterView> event) {
        RasterView rasterView = event.getEntity();
        pubScopesDl.load();
        pubScopesDl.getContainer().getItems().stream()
            .filter(DataProduct_PubScope::getDefaultValue)
            .findFirst()
            .ifPresent(rasterView::setPubScope);
    }
}