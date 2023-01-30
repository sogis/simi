package ch.so.agi.simi.web.screens.theme.pubsub;

import ch.so.agi.simi.entity.theme.subarea.PublishedSubArea;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.RemoveOperation;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.dbview.PublishedSubAreaListItem;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@UiController("simiTheme_PublishedSubAreaListItem.browse")
@UiDescriptor("published-sub-area-list-item-browse.xml")
@LookupComponent("publishedSubAreaListItemsTable")
@LoadDataBeforeShow
public class PublishedSubAreaListItemBrowse extends StandardLookup<PublishedSubAreaListItem> {
    @Inject
    private ScreenBuilders screenBuilders;

    @Inject
    private CollectionLoader<PublishedSubAreaListItem> publishedSubAreaListItemsDl;

    @Inject
    private Table<PublishedSubAreaListItem> publishedSubAreaListItemsTable;

    @Inject
    private DataManager manager;


    @Subscribe("publishedSubAreaListItemsTable.create")
    public void onRowCreate(Action.ActionPerformedEvent event) {
        screenBuilders.editor(PublishedSubArea.class, this)
                .withOpenMode(OpenMode.DIALOG)
                .withScreenClass(PublishedSubAreaCreate.class)
                .withAfterCloseListener((afterCloseEvent) -> publishedSubAreaListItemsDl.load())
                .show();
    }

    @Subscribe("publishedSubAreaListItemsTable.edit")
    public void onRowEdit(Action.ActionPerformedEvent event) {
        List<PublishedSubArea> editObj = loadPsubsFromSelection();

        screenBuilders.editor(PublishedSubArea.class, this)
                .editEntity(editObj.get(0))
                .show();
    }

    @Subscribe("publishedSubAreaListItemsTable.remove")
    public void onRowsRemove(Action.ActionPerformedEvent event) {
        List<PublishedSubArea> removeObj = loadPsubsFromSelection();

        CommitContext trans = new CommitContext();
        for(PublishedSubArea obj : removeObj)
            trans.addInstanceToRemove(obj);

        manager.commit(trans);

        publishedSubAreaListItemsDl.load();
    }

    private List<PublishedSubArea> loadPsubsFromSelection(){
        Set<PublishedSubAreaListItem> selected = publishedSubAreaListItemsTable.getSelected();
        List<UUID> idList = selected.stream().map(entity -> entity.getId()).collect(Collectors.toList());

        return manager.load(PublishedSubArea.class)
                .ids(idList)
                .list();
    }
}