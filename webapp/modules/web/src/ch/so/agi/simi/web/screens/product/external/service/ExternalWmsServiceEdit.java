package ch.so.agi.simi.web.screens.product.external.service;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.ExternalWmsService;

@UiController("simiProduct_ExternalWmsService.edit")
@UiDescriptor("external-wms-service-edit.xml")
@EditedEntityContainer("externalWmsServiceDc")
@LoadDataBeforeShow
public class ExternalWmsServiceEdit extends StandardEditor<ExternalWmsService> {
}