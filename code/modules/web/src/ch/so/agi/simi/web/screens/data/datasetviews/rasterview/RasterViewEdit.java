package ch.so.agi.simi.web.screens.data.datasetviews.rasterview;

import ch.so.agi.simi.entity.data.datasetview.RasterView;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("simiData_RasterView.edit")
@UiDescriptor("raster-view-edit.xml")
@EditedEntityContainer("dataProductDc")
@LoadDataBeforeShow
public class RasterViewEdit extends StandardEditor<RasterView> {
    /*
    @Inject
    private CollectionPropertyContainer<Permission> permissionsDc;

     */
    @Inject
    private DataContext dataContext;
    @Inject
    private InstanceContainer<RasterView> dataProductDc;
    @Inject
    private Messages messages;
    @Inject
    private TextField<String> searchFilterWordField;
    /*
    @Inject
    private Table<Permission> permissionsTable;

    @Subscribe("addPermissionBtn")
    public void onAddPermissionBtnClick(Button.ClickEvent event) {
        Permission permission = dataContext.create(Permission.class);
        permission.setDataSetView(this.getEditedEntity());

        permissionsDc.getMutableItems().add(permission);

        permissionsTable.requestFocus(permission, "role");
    }

     */
}