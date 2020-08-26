package ch.so.agi.simi.web.screens.product.map;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.Map;

@UiController("simi_Map.edit")
@UiDescriptor("map-edit.xml")
@EditedEntityContainer("mapDc")
@LoadDataBeforeShow
public class MapEdit extends StandardEditor<Map> {
}