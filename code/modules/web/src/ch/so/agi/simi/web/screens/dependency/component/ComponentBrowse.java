package ch.so.agi.simi.web.screens.dependency.component;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.dependency.Component;

@UiController("simiDependency_Component.browse")
@UiDescriptor("component-browse.xml")
@LookupComponent("componentsTable")
@LoadDataBeforeShow
public class ComponentBrowse extends StandardLookup<Component> {
}