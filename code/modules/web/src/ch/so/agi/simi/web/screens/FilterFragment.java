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

/*
* Is used in Browse-Screens to limit the resultlist with the user entered filter wordfragment.
*
* The wordfragment is set as parameter "term" on the dataloader of the parent screen. The structure
* of the whereclause is typically defined in the "<condition>" element for the dataloader in the
* xml screen description of the parent screen.
*/
@UiController("simi_FilterFragment")
@UiDescriptor("filter-fragment.xml")
public class FilterFragment<T extends Entity<T>> extends ScreenFragment {

    @Inject
    private TextField<String> fldQuickFilter;

    private CollectionLoader<T> dataLoader;
    @Inject
    private Button btnClearFilter;

    public void setDataLoader(CollectionLoader<T> dataLoader) {
        this.dataLoader = dataLoader;
    }

    private String inputPrompt;

    public void setInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;
    }

    @Subscribe("btnQuickFilter")
    public void onBtnQuickFilterClick(Button.ClickEvent event) {
        applyFilter();
    }

    @Subscribe("btnClearFilter")
    public void onBtnClearFilterClick(Button.ClickEvent event) {
        setFilter("");
    }

    @Subscribe("fldQuickFilter")
    public void onFldQuickFilterEnterPress(TextInputField.EnterPressEvent event) {
        applyFilter();
    }

    @Subscribe
    public void onAttach(AttachEvent event) {
        fldQuickFilter.setInputPrompt(inputPrompt);
    }

    private void applyFilter(){
        boolean validWhereClause = false;

        String term = fldQuickFilter.getValue();

        if(term != null){
            term = term.trim();
            if(term.length() > 0) {
                validWhereClause = true;

                term = '%' + term.toLowerCase() + '%';
            }
        }

        if(validWhereClause){
            dataLoader.setParameter("term", term);
            btnClearFilter.setVisible(true);
        }
        else {
            dataLoader.removeParameter("term");
            btnClearFilter.setVisible(false);
        }

        dataLoader.load();
    }

    public void setFilter(String filterString) {
        fldQuickFilter.setValue(filterString);

        applyFilter();
    }
}