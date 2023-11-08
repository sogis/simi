package ch.so.agi.simi.web.screens.data.datasetviews.tableview;

import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.data.TableField;
import ch.so.agi.simi.entity.data.datasetview.TableView;
import ch.so.agi.simi.entity.data.datasetview.ViewField;
import ch.so.agi.simi.entity.product.SingleActor;
import ch.so.agi.simi.web.beans.sort.SortBean;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
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
    @Inject
    private Label<String> formQgsStateLabel;
    @Inject
    private Label<String> formJsonStateLabel;
    @Inject
    private FileUploadField formJsonUploadBtn;
    @Inject
    private FileUploadField formQgsUploadBtn;
    @Inject
    private InstanceContainer<TableView> dataProductDc;


    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        this.getEditedEntity().updateDerivedIdentifier(dataManager);
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        loadTableFields();
        refreshFormStateLabel(true);
        refreshFormStateLabel(false);
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
        addMissingViewFields(0);
    }

    @Subscribe("btnRefAll")
    public void onBtnRefAllClick(Button.ClickEvent event) {
        addMissingViewFields(500);
    }

    private void addMissingViewFields(int startSortIndex) {
        HashSet<String> existingFields = new HashSet<>();
        if(viewFieldsDc.getMutableItems() != null){
            for(ViewField vf : viewFieldsDc.getMutableItems()){
                existingFields.add(vf.getTableField().getName());
            }
        }

        List<ViewField> viewFields = new ArrayList<>();

        int i=startSortIndex;
        for(TableField tf : tableFieldsDl.getContainer().getItems()){

            if(!existingFields.contains(tf.getName())) {

                ViewField f = dataContext.create(ViewField.class);
                f.setSort(i);
                i += 10;
                f.setTableField(tf);
                f.setWgcExposed(false);
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

    @Subscribe("formJsonUploadBtn")
    public void onFormJsonUploadBtnFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) {
        String fileContent = extractStringFromUpload(formJsonUploadBtn);
        TableView tv = dataProductDc.getItem();

        tv.setFormJson(fileContent);
        refreshFormStateLabel(true);
    }

    @Subscribe("formQgsUploadBtn")
    public void onFormQgsUploadBtnFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) {
        String fileContent = extractStringFromUpload(formQgsUploadBtn);
        TableView tv = dataProductDc.getItem();

        tv.setFormQgs(fileContent);
        refreshFormStateLabel(false);
    }

    private String extractStringFromUpload(FileUploadField uploadBtn){
        String uploaded = null;

        try {
            InputStream is = uploadBtn.getFileContent();
            byte[] bytes  = is.readAllBytes();
            uploaded = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return uploaded;
    }

    @Subscribe("formJsonDeleteBtn")
    public void onFormJsonDeleteBtnClick(Button.ClickEvent event) {
        TableView tv = dataProductDc.getItem();
        tv.setFormJson(null);
        refreshFormStateLabel(true);
    }

    @Subscribe("formQgsDeleteBtn")
    public void onFormQgsDeleteBtnClick(Button.ClickEvent event) {
        TableView tv = dataProductDc.getItem();
        tv.setFormQgs(null);
        refreshFormStateLabel(false);
    }

    private void refreshFormStateLabel(boolean jsonForm){
        Label<String> uploadStateLabel = null;
        boolean fieldIsNull = false;
        TableView tv = dataProductDc.getItem();

        if(jsonForm) {
            uploadStateLabel = formJsonStateLabel;
            fieldIsNull = tv.getFormJson() == null;
        }
        else {
            uploadStateLabel = formQgsStateLabel;
            fieldIsNull = tv.getFormQgs() == null;
        }

        if(fieldIsNull)
            uploadStateLabel.setValue("Nicht vorh.");
        else
            uploadStateLabel.setValue("Vorhanden");
    }

    @Subscribe("formJsonDownloadBtn")
    public void onFormJsonDownloadBtnClick(Button.ClickEvent event) {
        downloadFormData(true);
    }

    @Subscribe("formQgsDownloadBtn")
    public void onFormQgsDownloadBtnClick(Button.ClickEvent event) {
        downloadFormData(false);
    }

    private void downloadFormData(boolean fromJsonField){
        TableView tv = dataProductDc.getItem();
        String dataString = null;
        String fileSuffix = null;
        if(fromJsonField) {
            dataString = tv.getFormJson();
            fileSuffix = "json";
        }
        else {
            dataString = tv.getFormQgs();
            fileSuffix = "qgs";
        }

        if(dataString == null)
            return;

        byte[] data = dataString.getBytes(StandardCharsets.UTF_8);
        String fileName = tv.getDerivedIdentifier() + "." + fileSuffix;

        AppConfig.createExportDisplay(null).show(
                new ByteArrayDataProvider(data),
                fileName
        );
    }
}
