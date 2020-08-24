package ch.so.agi.simi.web.screens.identity;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.iam.Identity;

@UiController("simi_Identity.browse")
@UiDescriptor("identity-browse.xml")
@LookupComponent("identitiesTable")
@LoadDataBeforeShow
public class IdentityBrowse extends StandardLookup<Identity> {
}