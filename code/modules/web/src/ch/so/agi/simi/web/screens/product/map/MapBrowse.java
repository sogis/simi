package ch.so.agi.simi.web.screens.product.map;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.Map;

@UiController("simiProduct_Map.browse")
@UiDescriptor("map-browse.xml")
@LookupComponent("mapsTable")
@LoadDataBeforeShow
public class MapBrowse extends StandardLookup<Map> {
}