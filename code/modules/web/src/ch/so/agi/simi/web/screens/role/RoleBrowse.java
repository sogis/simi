package ch.so.agi.simi.web.screens.role;

import ch.so.agi.simi.entity.iam.User;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.iam.Role;

import javax.inject.Inject;

@UiController("simi_Role.browse")
@UiDescriptor("role-browse.xml")
@LookupComponent("rolesTable")
@LoadDataBeforeShow
public class RoleBrowse extends StandardLookup<Role> {
    @Inject
    private TextField<String> fldQuickFilter;
    @Inject
    private CollectionLoader<Role> rolesDl;

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
            rolesDl.setParameter("term", term);
        }
        else {
            rolesDl.removeParameter("term");
        }

        rolesDl.load();
    }
}