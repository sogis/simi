package ch.so.agi.simi.web.screens.role;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.iam.Role;

@UiController("simi_Role.edit")
@UiDescriptor("role-edit.xml")
@EditedEntityContainer("roleDc")
@LoadDataBeforeShow
public class RoleEdit extends StandardEditor<Role> {
}