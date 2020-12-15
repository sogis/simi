package ch.so.agi.simi.web.screens.dependency.component;

import ch.so.agi.simi.entity.dependency.Component;
import ch.so.agi.simi.entity.dependency.Report;
import com.haulmont.cuba.gui.screen.*;

@UiController("simiDependency_Component.edit")
@UiDescriptor("component-edit.xml")
@EditedEntityContainer("componentDc")
@LoadDataBeforeShow
public class ComponentEdit extends StandardEditor<Component> {
}