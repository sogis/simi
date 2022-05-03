package ch.so.agi.simi.web.screens.product.external.service;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.ExternalWmsService;

@UiController("simiProduct_ExternalWmsService.browse")
@UiDescriptor("external-wms-service-browse.xml")
@LookupComponent("externalWmsServicesTable")
@LoadDataBeforeShow
public class ExternalWmsServiceBrowse extends StandardLookup<ExternalWmsService> {
}