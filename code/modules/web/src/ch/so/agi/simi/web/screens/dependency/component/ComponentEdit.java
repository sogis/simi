package ch.so.agi.simi.web.screens.dependency.component;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.dependency.Component;

@UiController("simiDependency_Component.edit")
@UiDescriptor("component-edit.xml")
@EditedEntityContainer("componentDc")
@LoadDataBeforeShow
public class ComponentEdit extends StandardEditor<Component> {
}