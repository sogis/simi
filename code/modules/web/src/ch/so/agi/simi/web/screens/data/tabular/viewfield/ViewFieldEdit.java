package ch.so.agi.simi.web.screens.data.tabular.viewfield;

import ch.so.agi.simi.entity.data.TableField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.datasetview.ViewField;

import javax.inject.Inject;

@UiController("simiData_ViewField.edit")
@UiDescriptor("view-field-edit.xml")
@EditedEntityContainer("viewFieldDc")
@LoadDataBeforeShow
public class ViewFieldEdit extends StandardEditor<ViewField> {
    @Inject
    private CollectionLoader<TableField> tableFieldsDl;
    @Inject
    private InstanceContainer<ViewField> viewFieldDc;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        tableFieldsDl.setParameter("table", viewFieldDc.getItem().getTableView().getPostgresTable());
        tableFieldsDl.load();
    }
}