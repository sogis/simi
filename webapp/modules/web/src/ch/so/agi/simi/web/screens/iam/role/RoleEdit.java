package ch.so.agi.simi.web.screens.iam.role;

import ch.so.agi.simi.entity.iam.User;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.iam.Role;

import javax.inject.Inject;

@UiController("simiIAM_Role.edit")
@UiDescriptor("role-edit.xml")
@EditedEntityContainer("roleDc")
@LoadDataBeforeShow
public class RoleEdit extends StandardEditor<Role> {
    @Inject
    private UiComponents uiComponents;
    @Inject
    private MetadataTools metadataTools;

    @Install(to = "usersTable.instanceName", subject = "columnGenerator")
    private Component usersTableInstanceNameColumnGenerator(User user) {
        Label label = uiComponents.create(Label.NAME);
        label.setValue(metadataTools.getInstanceName(user));
        return label;
    }
}