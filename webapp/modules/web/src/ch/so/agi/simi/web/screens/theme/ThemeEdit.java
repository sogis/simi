package ch.so.agi.simi.web.screens.theme;

import ch.so.agi.simi.global.validation.JsonArrayUtil;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.Theme;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextArea;

import javax.inject.Inject;

@UiController("simiTheme_Theme.edit")
@UiDescriptor("theme-edit.xml")
@EditedEntityContainer("themeDc")
@LoadDataBeforeShow
public class ThemeEdit extends StandardEditor<Theme> {

    @Inject
    private TextArea<String> keywordsField;

    @Inject
    private TextArea<String> synonymsField;

    @Subscribe("synonyms2JsonBtn")
    private void onSynonyms2JsonBtnClick(Button.ClickEvent event) {
        convertToJsonArray(synonymsField);
    }

    @Subscribe("keywords2JsonBtn")
    private void onKeywords2JsonBtnClick(Button.ClickEvent event) {
        convertToJsonArray(keywordsField);
    }

    private void convertToJsonArray(TextArea<String> textArea){
        String content = textArea.getRawValue();

        String converted = JsonArrayUtil.tryConvertToJsonArray(content);

        textArea.setValue(converted);
    }
}