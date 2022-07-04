package ch.so.agi.simi.web.screens.product.therm;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.ThermGroup;

import javax.inject.Inject;

@UiController("simiProduct_ThermGroup.edit")
@UiDescriptor("therm-group-edit.xml")
@EditedEntityContainer("thermGroupDc")
@LoadDataBeforeShow
public class ThermGroupEdit extends StandardEditor<ThermGroup> {

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

        if(content == null || content.startsWith("["))
            return;

        String[] elem1 = content.split(",");
        if(elem1 == null)
            elem1 = new String[]{content};

        String[] elem2 = content.split(";");
        if(elem2 == null)
            elem2 = new String[]{content};

        String[] split;
        if(elem1.length > elem2.length)
            split = elem1;
        else
            split = elem2;

        for(int i=0; i<split.length; i++){
            String s = split[i];
            split[i] = "\"" + s.trim() + "\"";
        }

        String res = "[" + String.join(",", split) + "]";

        textArea.setValue(res);
    }
}