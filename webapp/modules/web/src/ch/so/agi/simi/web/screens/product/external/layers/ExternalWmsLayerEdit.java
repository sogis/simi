package ch.so.agi.simi.web.screens.product.external.layers;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.ExternalWmsLayer;

import javax.inject.Inject;

@UiController("simiProduct_ExternalWmsLayers.edit")
@UiDescriptor("external-wms-layer-edit.xml")
@EditedEntityContainer("dataProductDc")
@LoadDataBeforeShow
public class ExternalWmsLayerEdit extends StandardEditor<ExternalWmsLayer> {
    @Inject
    DataManager dataManager;

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        this.getEditedEntity().updateDerivedIdentifier(dataManager);
    }
}