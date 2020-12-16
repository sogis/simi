package ch.so.agi.simi.web.screens.dependency.cccintegration;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.dependency.CCCIntegration;

@UiController("simiDependency_CCCIntegration.edit")
@UiDescriptor("ccc-integration-edit.xml")
@EditedEntityContainer("cCCIntegrationDc")
@LoadDataBeforeShow
public class CCCIntegrationEdit extends StandardEditor<CCCIntegration> {
}