package ch.so.agi.simi.web.screens.ccc.notifylayer;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.ccc.NotifyLayer;

@UiController("simiCCC_NotifyLayer.edit")
@UiDescriptor("notify-layer-edit.xml")
@EditedEntityContainer("notifyLayerDc")
@LoadDataBeforeShow
public class NotifyLayerEdit extends StandardEditor<NotifyLayer> {
}