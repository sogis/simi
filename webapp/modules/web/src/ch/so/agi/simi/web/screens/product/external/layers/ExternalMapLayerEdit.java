package ch.so.agi.simi.web.screens.product.external.layers;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.ExternalMapLayer;

@UiController("simiProduct_ExternalMapLayers.edit")
@UiDescriptor("external-map-layer-edit.xml")
@EditedEntityContainer("dataProductDc")
@LoadDataBeforeShow
public class ExternalMapLayerEdit extends StandardEditor<ExternalMapLayer> {
}