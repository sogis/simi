package ch.so.agi.simi.web.screens.dependency.component;

import ch.so.agi.simi.entity.extended.Component;
import com.haulmont.cuba.gui.screen.*;

@UiController("simiExtended_Component.edit")
@UiDescriptor("component-edit.xml")
@EditedEntityContainer("componentDc")
@LoadDataBeforeShow
public class ComponentEdit extends StandardEditor<Component> {
}