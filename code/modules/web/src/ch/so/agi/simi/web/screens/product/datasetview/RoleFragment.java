package ch.so.agi.simi.web.screens.product.datasetview;

import ch.so.agi.simi.entity.iam.Permission;
import ch.so.agi.simi.entity.iam.Role;
import ch.so.agi.simi.entity.product.datasetview.DataSetView;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("simi_RoleFragment")
@UiDescriptor("role-fragment.xml")
public class RoleFragment extends ScreenFragment {

    @Inject
    private CollectionPropertyContainer<Permission> permissionsDc;
    @Inject
    private DataContext dataContext;
    @Inject
    private Table<Permission> permissionsTable;

    @Inject
    private CollectionLoader<Role> rolesDl;

    @Subscribe(target = Target.PARENT_CONTROLLER)
    private void onBeforeShowHost(Screen.BeforeShowEvent event) {
        rolesDl.load();
    }

    @Subscribe("addPermissionBtn")
    public void onAddPermissionBtnClick(Button.ClickEvent event) {
        Permission permission = dataContext.create(Permission.class);
        permission.setDataSetView( ((StandardEditor<DataSetView>)this.getHostScreen()).getEditedEntity()  );

        // insert new permission to table
        permissionsDc.getMutableItems().add(permission);

        // set focust to the new permission
        permissionsTable.requestFocus(permission, "role");
    }
}