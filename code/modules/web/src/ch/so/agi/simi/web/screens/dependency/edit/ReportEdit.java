package ch.so.agi.simi.web.screens.dependency.edit;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.dependency.Report;

@UiController("simiDependency_Report.edit")
@UiDescriptor("report-edit.xml")
@EditedEntityContainer("reportDc")
@LoadDataBeforeShow
public class ReportEdit extends StandardEditor<Report> {
}