package ch.so.agi.simi.web.screens.theme.themepub;

import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.entity.theme.ThemePublication_TypeEnum;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("simiTheme_ThemePublication.edit")
@UiDescriptor("theme-publication-edit.xml")
@EditedEntityContainer("themePublicationDc")
@LoadDataBeforeShow
public class ThemePublicationEdit extends StandardEditor<ThemePublication> {

    @Inject
    TextField<String> identSuffixField;

    @Subscribe("dataClassField")
    public void onDataClassFieldValueChange(HasValue.ValueChangeEvent<ThemePublication_TypeEnum> event) {
        proposeSuffix(event.getPrevValue(), event.getValue());
    }

    private void proposeSuffix(ThemePublication_TypeEnum prev, ThemePublication_TypeEnum current){

        if(prev == null || current == null)
            return;

        String oldDefault = prev.getDefaultSuffix();
        String newDefault = current.getDefaultSuffix();

        String val = identSuffixField.getValue();
        if(val == null)
            val = "";

        if(val.equals(oldDefault)){
            if(newDefault.equals(""))
                identSuffixField.setValue(null);
            else
                identSuffixField.setValue(newDefault);
        }
    }
}