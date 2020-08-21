package ch.so.agi.simi.web.screens.group;

import ch.so.agi.simi.entity.iam.User;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.iam.Group;

import javax.inject.Inject;

@UiController("simi_Group.browse")
@UiDescriptor("group-browse.xml")
@LookupComponent("groupsTable")
@LoadDataBeforeShow
public class GroupBrowse extends StandardLookup<Group> {
    @Inject
    private TextField<String> fldQuickFilter;
    @Inject
    private CollectionLoader<Group> groupsDl;

    @Subscribe("btnQuickFilter")
    public void onBtnQuickFilterClick(Button.ClickEvent event) {
        filterSingleActors();
    }

    @Subscribe("fldQuickFilter")
    public void onFldQuickFilterEnterPress(TextInputField.EnterPressEvent event) {
        filterSingleActors();
    }

    private void filterSingleActors(){
        boolean validWhereClause = false;

        String term = fldQuickFilter.getValue();

        if(term != null){
            term = term.trim();
            if(term.length() > 0) {
                validWhereClause = true;

                term = '%' + term + '%';
            }
        }

        if(validWhereClause){
            groupsDl.setParameter("term", term);
        }
        else {
            groupsDl.removeParameter("term");
        }

        groupsDl.load();
    }
}