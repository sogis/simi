package ch.so.agi.simi.web.screens.role;

import ch.so.agi.simi.entity.iam.Identity;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.iam.Role;

import javax.inject.Inject;

@UiController("simi_Role.edit")
@UiDescriptor("role-edit.xml")
@EditedEntityContainer("roleDc")
@LoadDataBeforeShow
public class RoleEdit extends StandardEditor<Role> {
    @Inject
    private UiComponents uiComponents;
    @Inject
    private MetadataTools metadataTools;

//    @Install(to = "identitiesTable.instanceName", subject = "columnGenerator")
//    private Component identitiesTableInstanceNameColumnGenerator(Identity identity) {
//        Label label = uiComponents.create(Label.NAME);
//        //label.setValue(metadataTools.getInstanceName(identity));
//        label.setValue("generated column");
//        return label;
//    }
}