package ch.so.agi.simi.web.screens.layerrelation;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.featureinfo.LayerRelation;

@UiController("fi_LayerRelation.edit")
@UiDescriptor("layer-relation-edit.xml")
@EditedEntityContainer("layerRelationDc")
@LoadDataBeforeShow
public class LayerRelationEdit extends StandardEditor<LayerRelation> {
}