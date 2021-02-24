package ch.so.agi.simi.web.screens.product.tableview;

import ch.so.agi.simi.entity.data.TableField;
import ch.so.agi.simi.entity.product.datasetview.TableView;
import ch.so.agi.simi.entity.product.datasetview.ViewField;
import ch.so.agi.simi.entity.iam.Permission;
import ch.so.agi.simi.web.beans.sort.SortBean;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.List;

@UiController("simiData_TableView.edit")
@UiDescriptor("table-view-edit.xml")
@EditedEntityContainer("dataProductDc")
@LoadDataBeforeShow
public class TableViewEdit extends StandardEditor<TableView> {
    @Inject
    private Table<ViewField> viewFieldsTable;
    @Inject
    private CollectionPropertyContainer<ViewField> viewFieldsDc;
    @Inject
    private DataContext dataContext;
    @Inject
    private CollectionLoader<TableField> tableFieldsDl;
    @Inject
    private SortBean sortBean;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        tableFieldsDl.setParameter("table", this.getEditedEntity().getPostgresTable());
        tableFieldsDl.load();
    }

    @Subscribe("viewFieldsTable.sortAction")
    public void onSingleActorsTableSortAction(Action.ActionPerformedEvent event) {
        viewFieldsTable.sort("sort", Table.SortDirection.ASCENDING);
    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPreCommit(DataContext.PreCommitEvent event) {
        List<ViewField> entities = sortBean.AdjustSort(viewFieldsDc.getItems());

        //add modified instances to the commit list
        event.getModifiedInstances().addAll(entities);
    }

    @Subscribe("addViewFieldBtn")
    public void onAddViewFieldBtnClick(Button.ClickEvent event) {
        ViewField viewField = dataContext.create(ViewField.class);
        viewField.setTableView(this.getEditedEntity());

        // insert new viewField to table
        viewFieldsDc.getMutableItems().add(viewField);

        // set focus on the new viewField
        viewFieldsTable.requestFocus(viewField, "sort");
    }
}
