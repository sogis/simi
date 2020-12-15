package ch.so.agi.simi.web.screens.dependency.browse;

import ch.so.agi.simi.entity.dependency.Component;
import ch.so.agi.simi.entity.dependency.FeatureInfo;
import ch.so.agi.simi.entity.dependency.Report;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.dependency.Dependency;

import javax.inject.Inject;

@UiController("simiDependency_Dependency.browse")
@UiDescriptor("dependency-browse.xml")
@LookupComponent("dependenciesTable")
@LoadDataBeforeShow
public class DependencyBrowse extends StandardLookup<Dependency> {
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Table<Dependency> dependenciesTable;
    @Inject
    private Metadata metdata;

    @Subscribe("createBtn.createReport")
    public void onCreateBtnCreateReport(Action.ActionPerformedEvent event) {
        Report report = metdata.create(Report.class);
        showCreateEditorForDependency(report);
    }

    @Subscribe("createBtn.createComponent")
    public void onCreateBtnCreateComponent(Action.ActionPerformedEvent event) {
        Component comp = metdata.create(Component.class);
        showCreateEditorForDependency(comp);
    }

    @Subscribe("createBtn.createFeatureinfo")
    public void onCreateBtnCreateFeatureinfo(Action.ActionPerformedEvent event) {
        FeatureInfo fi = metdata.create(FeatureInfo.class);
        showCreateEditorForDependency(fi);
    }

    private void showCreateEditorForDependency(Dependency dependency) {
        screenBuilders.editor(dependenciesTable)
                .editEntity(dependency)
                .build()
                .show();
    }
}