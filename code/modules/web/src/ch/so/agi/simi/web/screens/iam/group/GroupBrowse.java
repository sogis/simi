package ch.so.agi.simi.web.screens.iam.group;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.iam.Group;

@UiController("simi_Group.browse")
@UiDescriptor("group-browse.xml")
@LookupComponent("groupsTable")
@LoadDataBeforeShow
public class GroupBrowse extends StandardLookup<Group> {
}