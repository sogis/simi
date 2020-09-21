package ch.so.agi.simi.web.screens.ccc.cccclient;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.ccc.CCCClient;

@UiController("simiCCC_CCCClient.edit")
@UiDescriptor("ccc-client-edit.xml")
@EditedEntityContainer("cCCClientDc")
@LoadDataBeforeShow
public class CCCClientEdit extends StandardEditor<CCCClient> {
}