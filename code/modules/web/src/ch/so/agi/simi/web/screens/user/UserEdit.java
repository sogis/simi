package ch.so.agi.simi.web.screens.user;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.iam.User;

@UiController("simi_User.edit")
@UiDescriptor("user-edit.xml")
@EditedEntityContainer("userDc")
@LoadDataBeforeShow
public class UserEdit extends StandardEditor<User> {
}