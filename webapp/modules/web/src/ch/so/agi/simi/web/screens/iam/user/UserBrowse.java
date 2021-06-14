package ch.so.agi.simi.web.screens.iam.user;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.iam.User;

@UiController("simiIAM_User.browse")
@UiDescriptor("user-browse.xml")
@LookupComponent("usersTable")
@LoadDataBeforeShow
public class UserBrowse extends StandardLookup<User> {
}