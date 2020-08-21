package ch.so.agi.simi.web.screens.user;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.iam.User;

import javax.inject.Inject;

@UiController("simi_User.browse")
@UiDescriptor("user-browse.xml")
@LookupComponent("usersTable")
@LoadDataBeforeShow
public class UserBrowse extends StandardLookup<User> {
    @Inject
    private TextField<String> fldQuickFilter;
    @Inject
    private CollectionLoader<User> usersDl;

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
            usersDl.setParameter("term", term);
        }
        else {
            usersDl.removeParameter("term");
        }

        usersDl.load();
    }
}