package ch.so.agi.simi.web.screens.product.layergroup;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.LayerGroup;

@UiController("simiProduct_LayerGroup.lookup")
@UiDescriptor("layer-group-choose.xml")
@LookupComponent("layerGroupsTable")
@LoadDataBeforeShow
public class LayerGroupChoose extends StandardLookup<LayerGroup> {
}