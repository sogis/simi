package ch.so.agi.simi.web.screens.data.raster.rasterview;

import ch.so.agi.simi.entity.DataProduct_PubScope;
import ch.so.agi.simi.entity.data.raster.RasterView;
import ch.so.agi.simi.entity.iam.Permission;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("simiData_RasterView.edit")
@UiDescriptor("raster-view-edit.xml")
@EditedEntityContainer("rasterViewDc")
@LoadDataBeforeShow
public class RasterViewEdit extends StandardEditor<RasterView> {
    @Inject
    private CollectionLoader<DataProduct_PubScope> pubScopesDl;
    @Inject
    private CollectionPropertyContainer<Permission> permissionsDc;
    @Inject
    private DataContext dataContext;

    @Subscribe
    public void onInitEntity(InitEntityEvent<RasterView> event) {
        RasterView rasterView = event.getEntity();
        pubScopesDl.load();
        pubScopesDl.getContainer().getItems().stream()
            .filter(DataProduct_PubScope::getDefaultValue)
            .findFirst()
            .ifPresent(rasterView::setPubScope);
    }

    @Subscribe("addPermissionBtn")
    public void onAddPermissionBtnClick(Button.ClickEvent event) {
        Permission permission = dataContext.create(Permission.class);
        permission.setDataSetView(this.getEditedEntity());

        permissionsDc.getMutableItems().add(permission);
    }
}