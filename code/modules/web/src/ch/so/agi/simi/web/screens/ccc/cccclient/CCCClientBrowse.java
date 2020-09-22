package ch.so.agi.simi.web.screens.ccc.cccclient;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.ccc.CCCClient;

@UiController("simiCCC_CCCClient.browse")
@UiDescriptor("ccc-client-browse.xml")
@LookupComponent("cCCClientsTable")
@LoadDataBeforeShow
public class CCCClientBrowse extends StandardLookup<CCCClient> {
}