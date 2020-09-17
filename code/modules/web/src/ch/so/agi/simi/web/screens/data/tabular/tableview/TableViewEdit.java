package ch.so.agi.simi.web.screens.data.tabular.tableview;

import ch.so.agi.simi.entity.DataProduct_PubScope;
import ch.so.agi.simi.entity.data.tabular.TableView;
import ch.so.agi.simi.entity.data.tabular.ViewField;
import ch.so.agi.simi.entity.featureinfo.FeatureInfo;
import ch.so.agi.simi.entity.featureinfo.LayerRelation;
import ch.so.agi.simi.entity.iam.Permission;
import ch.so.agi.simi.entity.product.DataSetView_SearchTypeEnum;
import ch.so.agi.simi.web.StyleUploadDownloadBean;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import org.checkerframework.common.value.qual.ArrayLen;

import javax.inject.Inject;
import java.util.ArrayList;
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
    @Inject
    private CollectionPropertyContainer<Permission> permissionsDc;
    @Inject
    private DataContext dataContext;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Button createFeatureInfoBtn;
    @Inject
    private Button editFeatureInfoBtn;
    @Inject
    private Button clearFeatureInfoBtn;

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

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        refreshButtonVisibility();
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

    @Subscribe("addPermissionBtn")
    public void onAddPermissionBtnClick(Button.ClickEvent event) {
        Permission permission = dataContext.create(Permission.class);
        permission.setDataSetView(this.getEditedEntity());

        permissionsDc.getMutableItems().add(permission);
    }

    @Subscribe("clearFeatureInfoBtn")
    public void onClearFeatureInfoBtnClick(Button.ClickEvent event) {
        this.getEditedEntity().getLayerRelations().forEach(layerRelation -> dataContext.remove(layerRelation.getFeatureInfo()));
        this.getEditedEntity().getLayerRelations().clear();

        refreshButtonVisibility();
    }

    @Subscribe("createFeatureInfoBtn")
    public void onCreateFeatureInfoBtnClick(Button.ClickEvent event) {
        LayerRelation layerRelation = dataContext.create(LayerRelation.class);

        Screen screen = screenBuilders.editor(FeatureInfo.class, this)
                .newEntity()
                .withInitializer(featureInfo -> {
                    layerRelation.setDataSetView(this.getEditedEntity());
                    layerRelation.setFeatureInfo(featureInfo);

                    ArrayList<LayerRelation> layerRelations = new ArrayList<>();
                    layerRelations.add(layerRelation);

                    featureInfo.setLayerRelation(layerRelations);
                })
                .build();
        screen.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.closedWith(StandardOutcome.COMMIT)) {
                this.getEditedEntity().getLayerRelations().add(layerRelation);
            }
        });
        screen.show();

        refreshButtonVisibility();
    }

    @Subscribe("editFeatureInfoBtn")
    public void onEditFeatureInfoBtnClick(Button.ClickEvent event) {
        screenBuilders.editor(FeatureInfo.class, this)
                .editEntity(this.getEditedEntity().getLayerRelations().get(0).getFeatureInfo())
                .build()
                .show();
    }

    private void refreshButtonVisibility() {
        boolean createVisible = this.getEditedEntity().getLayerRelations().isEmpty();

        createFeatureInfoBtn.setVisible(createVisible);
        editFeatureInfoBtn.setVisible(!createVisible);
        clearFeatureInfoBtn.setVisible(!createVisible);
    }
}
