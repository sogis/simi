package ch.so.agi.simi.web.screens.featureinfo;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.featureinfo.FeatureInfo;

@UiController("fi_FeatureInfo.edit")
@UiDescriptor("feature-info-edit.xml")
@EditedEntityContainer("featureInfoDc")
@LoadDataBeforeShow
public class FeatureInfoEdit extends StandardEditor<FeatureInfo> {
}