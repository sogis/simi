package ch.so.agi.simi.web.screens.ccc.locatorlayer;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.ccc.LocatorLayer;

@UiController("simiCCC_LocatorLayer.edit")
@UiDescriptor("locator-layer-edit.xml")
@EditedEntityContainer("locatorLayerDc")
@LoadDataBeforeShow
public class LocatorLayerEdit extends StandardEditor<LocatorLayer> {
}