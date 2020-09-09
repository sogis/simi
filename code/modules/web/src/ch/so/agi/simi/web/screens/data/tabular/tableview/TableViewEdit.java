package ch.so.agi.simi.web.screens.data.tabular.tableview;

import ch.so.agi.simi.entity.DataProduct_PubScope;
import ch.so.agi.simi.entity.data.tabular.TableView;
import ch.so.agi.simi.entity.data.tabular.ViewField;
import ch.so.agi.simi.entity.product.DataSetView_SearchTypeEnum;
import ch.so.agi.simi.web.StyleUploadDownloadBean;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
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
    private FileUploadField uploadStyleServerBtn;
    @Inject
    private FileUploadField uploadStyleDesktopBtn;
    @Inject
    private TextField<String> searchFilterWordField;
    @Inject
    private Table<ViewField> viewFieldsTable;
    @Inject
    private CollectionPropertyContainer<ViewField> viewFieldsDc;
    @Inject
    private StyleUploadDownloadBean styleUploadDownloadBean;

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
        styleUploadDownloadBean.downloadString(tableView.getStyleServer(), tableView.getIdentifier() + ".Server.qml");
    }

    @Subscribe("uploadStyleServerBtn")
    public void onUploadStyleServerBtnFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) {
        styleUploadDownloadBean.handleFileUploadSucceed(uploadStyleServerBtn, content -> tableViewDc.getItem().setStyleServer(content));
    }

    @Subscribe("downloadStyleDesktopBtn")
    public void onDownloadStyleDesktopBtnClick(Button.ClickEvent event) {
        TableView tableView = tableViewDc.getItem();
        styleUploadDownloadBean.downloadString(tableView.getStyleDesktop(), tableView.getIdentifier() + ".Desktop.qml");
    }

    @Subscribe("uploadStyleDesktopBtn")
    public void onUploadStyleDesktopBtnFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) {
        styleUploadDownloadBean.handleFileUploadSucceed(uploadStyleDesktopBtn, content -> tableViewDc.getItem().setStyleDesktop(content));
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
