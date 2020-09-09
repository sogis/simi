package ch.so.agi.simi.web.screens.data.tabular.tableview;

import ch.so.agi.simi.entity.DataProduct_PubScope;
import ch.so.agi.simi.entity.data.tabular.TableView;
import ch.so.agi.simi.entity.data.tabular.ViewField;
import ch.so.agi.simi.entity.product.DataSetView_SearchTypeEnum;
import ch.so.agi.simi.web.UploadStyleBean;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@UiController("simiData_TableView.edit")
@UiDescriptor("table-view-edit.xml")
@EditedEntityContainer("tableViewDc")
@LoadDataBeforeShow
public class TableViewEdit extends StandardEditor<TableView> {
    @Inject
    private CollectionLoader<DataProduct_PubScope> pubScopesDl;
    @Inject
    private InstanceContainer<TableView> tableViewDc;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private Notifications notifications;
    @Inject
    private FileUploadField uploadStyleServerBtn;
    @Inject
    private FileUploadField uploadStyleDesktopBtn;
    @Inject
    private Dialogs dialogs;
    @Inject
    private TextField<String> searchFilterWordField;
    @Inject
    private Table<ViewField> viewFieldsTable;
    @Inject
    private CollectionPropertyContainer<ViewField> viewFieldsDc;
    @Inject
    private UploadStyleBean uploadStyleBean;

    @Subscribe
    public void onInitEntity(InitEntityEvent<TableView> event) {
        TableView tableView = event.getEntity();
        pubScopesDl.load();
        pubScopesDl.getContainer().getItems().stream()
            .filter(DataProduct_PubScope::getDefaultValue)
            .findFirst()
            .ifPresent(tableView::setPubScope);
    }

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        searchFilterWordField.addValidator(value -> {
            if (tableViewDc.getItem().getSearchType() != DataSetView_SearchTypeEnum.NEIN && (value == null || value.isEmpty()))
                throw  new ValidationException("Wenn Suchtyp 'Immer' oder 'falls geladen' ist, muss Filter Wort angegeben werden.");
        });
    }

    @Subscribe("downloadStyleServerBtn")
    public void onDownloadStyleServerBtnClick(Button.ClickEvent event) {
        TableView tableView = tableViewDc.getItem();
        downloadString(tableView.getStyleServer(), tableView.getIdentifier() + ".Server.qml");
    }

    @Subscribe("uploadStyleServerBtn")
    public void onUploadStyleServerBtnFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) {
        handleFileUploadSucceed(uploadStyleServerBtn, content -> tableViewDc.getItem().setStyleServer(content));
    }

    @Subscribe("downloadStyleDesktopBtn")
    public void onDownloadStyleDesktopBtnClick(Button.ClickEvent event) {
        TableView tableView = tableViewDc.getItem();
        downloadString(tableView.getStyleDesktop(), tableView.getIdentifier() + ".Desktop.qml");
    }

    @Subscribe("uploadStyleDesktopBtn")
    public void onUploadStyleDesktopBtnFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) {
        handleFileUploadSucceed(uploadStyleDesktopBtn, content -> tableViewDc.getItem().setStyleDesktop(content));
    }

    private void downloadString(String content, String filename) {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

        exportDisplay.show(new ByteArrayDataProvider(bytes), filename);
    }

    public void handleFileUploadSucceed(FileUploadField uploadField, Consumer<String> assignResult) {
        try {
            uploadStyleBean.checkUpload(uploadField.getFileContent(), fileContent -> {
                assignResult.accept(fileContent);
                notifications.create()
                        .withCaption(uploadField.getFileName() + " uploaded")
                        .show();
            });
        } catch (UploadStyleBean.StyleUploadException e) {
            dialogs.createMessageDialog()
                    .withCaption("Upload")
                    .withMessage(e.getLocalizedMessage())
                    .withType(Dialogs.MessageType.WARNING)
                    .show();
        }
    }

    @Subscribe("viewFieldsTable.sortAction")
    public void onSingleActorsTableSortAction(Action.ActionPerformedEvent event) {
        viewFieldsTable.sort("sort", Table.SortDirection.ASCENDING);
    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPreCommit(DataContext.PreCommitEvent event) {
        int i = 0;
        List<ViewField> viewFields = viewFieldsDc.getItems().stream()
                .sorted(Comparator.comparing(ViewField::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        // go through the data container items. The same can be done using getEditedEntity().getSingleActorList().
        for (ViewField item : viewFields) {
            // set new value and add modified instance to the commit list
            item.setSort(i);
            event.getModifiedInstances().add(item);
            i += 10;
        }
    }
}
