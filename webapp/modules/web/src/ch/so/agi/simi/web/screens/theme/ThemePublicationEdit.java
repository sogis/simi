package ch.so.agi.simi.web.screens.theme;

import ch.so.agi.simi.entity.theme.ThemePublication_TypeEnum;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.ThemePublication;

import javax.inject.Inject;

@UiController("simiTheme_ThemePublication.edit")
@UiDescriptor("theme-publication-edit.xml")
@EditedEntityContainer("themePublicationDc")
@LoadDataBeforeShow
public class ThemePublicationEdit extends StandardEditor<ThemePublication> {

    @Inject
    TextField<String> suffixField;

    @Subscribe("dataClassField")
    public void onDataClassFieldValueChange(HasValue.ValueChangeEvent<ThemePublication_TypeEnum> event) {
        refreshSuffix();
    }

    @Subscribe("classSuffixOverrideField")
    public void onClassSuffixOverrideFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        refreshSuffix();
    }

    private void refreshSuffix(){
        String ident = this.getEditedEntity().deferFullIdent();
        suffixField.setValue(ident);
    }
}