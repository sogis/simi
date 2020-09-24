package ch.so.agi.simi.web.screens.data.tabular.tableview;

import ch.so.agi.simi.entity.data.tabular.TableView;
import ch.so.agi.simi.entity.data.tabular.ViewField;
import ch.so.agi.simi.entity.featureinfo.FeatureInfo;
import ch.so.agi.simi.entity.iam.Permission;
import ch.so.agi.simi.entity.product.DataSetView;
import ch.so.agi.simi.entity.product.DataSetView_SearchTypeEnum;
import ch.so.agi.simi.web.screens.featureinfo.featureinfo.FeatureInfoEdit;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
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
@EditedEntityContainer("dataProductDc")
@LoadDataBeforeShow
public class TableViewEdit extends StandardEditor<TableView> {
    @Inject
    private InstanceContainer<TableView> dataProductDc;
    @Inject
    private TextField<String> searchFilterWordField;
    @Inject
    private Table<ViewField> viewFieldsTable;
    @Inject
    private CollectionPropertyContainer<ViewField> viewFieldsDc;
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
    @Inject
    private Label<String> featureInfoOverrideHint;
    @Inject
    private Messages messages;
    @Inject
    private Table<Permission> permissionsTable;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        searchFilterWordField.addValidator(value -> {
            if (dataProductDc.getItem().getSearchType() != DataSetView_SearchTypeEnum.NEIN && (value == null || value.isEmpty()))
                throw  new ValidationException("Wenn Suchtyp '" +
                        messages.getMessage(DataSetView_SearchTypeEnum.class, "DataSetView_SearchTypeEnum.IMMER") + "' oder '" +
                        messages.getMessage(DataSetView_SearchTypeEnum.class, "DataSetView_SearchTypeEnum.FALLS_GELADEN") + "' ist, muss '" +
                        messages.getMessage(DataSetView.class, "DataSetView.searchFilterWord") + "' angegeben werden.");
        });
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        refreshButtonVisibility();
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

    @Subscribe("addViewFieldBtn")
    public void onAddViewFieldBtnClick(Button.ClickEvent event) {
        ViewField viewField = dataContext.create(ViewField.class);
        viewField.setTableView(this.getEditedEntity());

        // insert new viewField to table
        viewFieldsDc.getMutableItems().add(viewField);

        // set focus on the new viewField
        viewFieldsTable.requestFocus(viewField, "sort");
    }

    @Subscribe("addPermissionBtn")
    public void onAddPermissionBtnClick(Button.ClickEvent event) {
        Permission permission = dataContext.create(Permission.class);
        permission.setDataSetView(this.getEditedEntity());

        // insert new permission to table
        permissionsDc.getMutableItems().add(permission);

        // set focust to the new permission
        permissionsTable.requestFocus(permission, "role");
    }

    @Subscribe("clearFeatureInfoBtn")
    public void onClearFeatureInfoBtnClick(Button.ClickEvent event) {
        dataContext.remove(this.getEditedEntity().getFeatureInfo());
        this.getEditedEntity().setFeatureInfo(null);

        refreshButtonVisibility();
    }

    @Subscribe("createFeatureInfoBtn")
    public void onCreateFeatureInfoBtnClick(Button.ClickEvent event) {
        Screen screen = screenBuilders.editor(FeatureInfo.class, this)
                .newEntity()
                .withInitializer(featureInfo -> {
                    featureInfo.setDataSetView(this.getEditedEntity());
                })
                .withParentDataContext(dataContext)
                .build();

        screen.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.closedWith(StandardOutcome.COMMIT)) {
                this.getEditedEntity().setFeatureInfo(((FeatureInfoEdit)afterCloseEvent.getScreen()).getEditedEntity());
            }

            refreshButtonVisibility();
        });
        screen.show();
    }

    @Subscribe("editFeatureInfoBtn")
    public void onEditFeatureInfoBtnClick(Button.ClickEvent event) {
        if (this.getEditedEntity().getFeatureInfo() == null) {
            throw new IllegalStateException("No FeatureInfo for editing found");
        } else {
            screenBuilders.editor(FeatureInfo.class, this)
                .editEntity(this.getEditedEntity().getFeatureInfo())
                .withParentDataContext(dataContext)
                .build()
                .show();
        }
    }

    private void refreshButtonVisibility() {
        boolean isCreateVisible = this.getEditedEntity().getFeatureInfo() == null;

        createFeatureInfoBtn.setVisible(isCreateVisible);
        editFeatureInfoBtn.setVisible(!isCreateVisible);
        clearFeatureInfoBtn.setVisible(!isCreateVisible);
        featureInfoOverrideHint.setVisible(!isCreateVisible);
    }
}
