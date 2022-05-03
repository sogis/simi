package ch.so.agi.simi.web.screens.product.external.layers;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.ExternalWmsLayer;

@UiController("simiProduct_ExternalWmsLayers.edit")
@UiDescriptor("external-wms-layer-edit.xml")
@EditedEntityContainer("dataProductDc")
@LoadDataBeforeShow
public class ExternalWmsLayerEdit extends StandardEditor<ExternalWmsLayer> {
}