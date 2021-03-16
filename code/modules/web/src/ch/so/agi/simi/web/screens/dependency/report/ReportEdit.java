package ch.so.agi.simi.web.screens.dependency.report;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.extended.Report;

@UiController("simiExtended_Report.edit")
@UiDescriptor("report-edit.xml")
@EditedEntityContainer("reportDc")
@LoadDataBeforeShow
public class ReportEdit extends StandardEditor<Report> {
}