package ch.so.agi.simi.web.screens.product.external.service;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.ExternalMapService;

@UiController("simiProduct_ExternalMapService.browse")
@UiDescriptor("external-map-service-browse.xml")
@LookupComponent("externalMapServicesTable")
@LoadDataBeforeShow
public class ExternalMapServiceBrowse extends StandardLookup<ExternalMapService> {
}