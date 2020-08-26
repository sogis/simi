package ch.so.agi.simi.web.screens.featureinfo.featureinfo;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.featureinfo.FeatureInfo;

@UiController("fi_FeatureInfo.browse")
@UiDescriptor("feature-info-browse.xml")
@LookupComponent("featureInfoesTable")
@LoadDataBeforeShow
public class FeatureInfoBrowse extends StandardLookup<FeatureInfo> {
}