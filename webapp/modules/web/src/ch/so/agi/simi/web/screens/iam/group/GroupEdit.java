package ch.so.agi.simi.web.screens.iam.group;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.iam.Group;

@UiController("simiIAM_Group.edit")
@UiDescriptor("group-edit.xml")
@EditedEntityContainer("groupDc")
@LoadDataBeforeShow
public class GroupEdit extends StandardEditor<Group> {
}