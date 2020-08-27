package ch.so.agi.simi.web.screens.product.layergroup;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.LayerGroup;

@UiController("simi_LayerGroup.edit")
@UiDescriptor("layer-group-edit.xml")
@EditedEntityContainer("layerGroupDc")
@LoadDataBeforeShow
public class LayerGroupEdit extends StandardEditor<LayerGroup> {
}