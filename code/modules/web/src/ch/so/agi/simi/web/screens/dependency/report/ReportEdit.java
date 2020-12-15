package ch.so.agi.simi.web.screens.dependency.report;

import ch.so.agi.simi.entity.dependency.Relation;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.dependency.Report;

import javax.inject.Inject;

@UiController("simiDependency_Report.edit")
@UiDescriptor("report-edit.xml")
@EditedEntityContainer("reportDc")
@LoadDataBeforeShow
public class ReportEdit extends StandardEditor<Report> {
}