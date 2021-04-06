package ch.so.agi.simi.web.screens.product.external.layers;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.ExternalMapLayers;

@UiController("simiProduct_ExternalMapLayers.edit")
@UiDescriptor("external-map-layers-edit.xml")
@EditedEntityContainer("dataProductDc")
@LoadDataBeforeShow
public class ExternalMapLayersEdit extends StandardEditor<ExternalMapLayers> {
}