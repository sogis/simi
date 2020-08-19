package ch.so.agi.simi.web.screens.layerrelation;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.featureinfo.LayerRelation;

@UiController("fi_LayerRelation.browse")
@UiDescriptor("layer-relation-browse.xml")
@LookupComponent("layerRelationsTable")
@LoadDataBeforeShow
public class LayerRelationBrowse extends StandardLookup<LayerRelation> {
}