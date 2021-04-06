package ch.so.agi.simi.web.screens.product.external.service;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.ExternalMapService;

@UiController("simiProduct_ExternalMapService.edit")
@UiDescriptor("external-map-service-edit.xml")
@EditedEntityContainer("externalMapServiceDc")
@LoadDataBeforeShow
public class ExternalMapServiceEdit extends StandardEditor<ExternalMapService> {
}