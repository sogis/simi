package ch.so.agi.simi.web.screens.extended.cccintegration;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.extended.CCCIntegration;

@UiController("simiExtended_CCCIntegration.edit")
@UiDescriptor("ccc-integration-edit.xml")
@EditedEntityContainer("cCCIntegrationDc")
@LoadDataBeforeShow
public class CCCIntegrationEdit extends StandardEditor<CCCIntegration> {
}