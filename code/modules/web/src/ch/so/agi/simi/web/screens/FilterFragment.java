package ch.so.agi.simi.web.screens;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;

@UiController("simi_FilterFragment")
@UiDescriptor("filter-fragment.xml")
public class FilterFragment<T extends Entity<T>> extends ScreenFragment {

    @Inject
    private TextField<String> fldQuickFilter;

    private CollectionLoader<T> dataLoader;

    public void setDataLoader(CollectionLoader<T> dataLoader) {
        this.dataLoader = dataLoader;
    }

    private String inputPrompt;

    public void setInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;
    }

    @Subscribe("btnQuickFilter")
    public void onBtnQuickFilterClick(Button.ClickEvent event) {
        filterSingleActors();
    }

    @Subscribe("fldQuickFilter")
    public void onFldQuickFilterEnterPress(TextInputField.EnterPressEvent event) {
        filterSingleActors();
    }

    @Subscribe
    public void onAttach(AttachEvent event) {
        fldQuickFilter.setInputPrompt(inputPrompt);
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
            dataLoader.setParameter("term", term);
        }
        else {
            dataLoader.removeParameter("term");
        }

        dataLoader.load();
    }
}