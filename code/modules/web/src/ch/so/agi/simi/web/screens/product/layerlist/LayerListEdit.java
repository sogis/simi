package ch.so.agi.simi.web.screens.product.layerlist;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.LayerList;

@UiController("simi_LayerList.edit")
@UiDescriptor("layer-list-edit.xml")
@EditedEntityContainer("layerListDc")
@LoadDataBeforeShow
public class LayerListEdit extends StandardEditor<LayerList> {
}