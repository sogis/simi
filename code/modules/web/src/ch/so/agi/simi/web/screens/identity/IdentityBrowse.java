package ch.so.agi.simi.web.screens.identity;

import ch.so.agi.simi.entity.iam.User;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.iam.Identity;

import javax.inject.Inject;

@UiController("simi_Identity.browse")
@UiDescriptor("identity-browse.xml")
@LookupComponent("identitiesTable")
@LoadDataBeforeShow
public class IdentityBrowse extends StandardLookup<Identity> {
    @Inject
    private TextField<String> fldQuickFilter;
    @Inject
    private CollectionLoader<Identity> identitiesDl;

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
            identitiesDl.setParameter("term", term);
        }
        else {
            identitiesDl.removeParameter("term");
        }

        identitiesDl.load();
    }
}