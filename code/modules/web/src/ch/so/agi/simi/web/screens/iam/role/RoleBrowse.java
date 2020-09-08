package ch.so.agi.simi.web.screens.iam.role;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.iam.Role;

@UiController("simiIAM_Role.browse")
@UiDescriptor("role-browse.xml")
@LookupComponent("rolesTable")
@LoadDataBeforeShow
public class RoleBrowse extends StandardLookup<Role> {
}