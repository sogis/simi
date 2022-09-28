package ch.so.agi.simi.web.screens.data.datasetviews.tableview;

import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.data.TableField;
import ch.so.agi.simi.entity.data.datasetview.TableView;
import ch.so.agi.simi.entity.data.datasetview.ViewField;
import ch.so.agi.simi.web.beans.sort.SortBean;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
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
    @Inject
    private DataManager dataManager;


    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        this.getEditedEntity().updateDerivedIdentifier(dataManager);
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        loadTableFields();
    }

    @Subscribe("postgresTableField")
    public void onPostgresTableFieldValueChange(HasValue.ValueChangeEvent<PostgresTable> event) {
        if(!event.isUserOriginated())
            return;


        for (ViewField f : viewFieldsDc.getMutableItems()) {
            dataContext.remove(f);
        }
        viewFieldsDc.getMutableItems().clear();

        loadTableFields();
        addMissingViewFields();
    }

    @Subscribe("btnRefAll")
    public void onBtnRefAllClick(Button.ClickEvent event) {
        addMissingViewFields();
    }

    private void addMissingViewFields() {
        HashSet<String> existingFields = new HashSet<>();
        if(viewFieldsDc.getMutableItems() != null){
            for(ViewField vf : viewFieldsDc.getMutableItems()){
                existingFields.add(vf.getTableField().getName());
            }
        }

        List<ViewField> viewFields = new ArrayList<>();

        int i=10;
        for(TableField tf : tableFieldsDl.getContainer().getItems()){

            if(!existingFields.contains(tf.getName())) {

                ViewField f = dataContext.create(ViewField.class);
                f.setSort(i);
                i += 10;
                f.setTableField(tf);
                f.setWgcExposed(true);
                f.setTableView(this.getEditedEntity());

                viewFields.add(f);
            }
        }

        viewFieldsDc.getMutableItems().addAll(viewFields);
    }

    private void loadTableFields(){
        if(this.getEditedEntity().getPostgresTable() == null)
            return;

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
