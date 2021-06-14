package ch.so.agi.simi.web.screens.extended.featureInfo;

import ch.so.agi.simi.entity.extended.Report;
import com.haulmont.cuba.gui.screen.*;

@UiController("simiExtended_FeatureInfo.edit")
@UiDescriptor("featureInfo-edit.xml")
@EditedEntityContainer("featureInfoDc")
@LoadDataBeforeShow
public class FeatureInfoEdit extends StandardEditor<Report> {
}