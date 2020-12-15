package ch.so.agi.simi.web.screens.dependency.featureInfo;

import ch.so.agi.simi.entity.dependency.Report;
import com.haulmont.cuba.gui.screen.*;

@UiController("simiDependency_FeatureInfo.edit")
@UiDescriptor("featureInfo-edit.xml")
@EditedEntityContainer("featureInfoDc")
@LoadDataBeforeShow
public class FeatureInfoEdit extends StandardEditor<Report> {
}