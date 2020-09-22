package ch.so.agi.simi.web.screens.featureinfo.featureinfo;

import ch.so.agi.simi.entity.featureinfo.FeatureInfo;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.function.Consumer;

@UiController("simiFeatureInfo_FeatureInfo.edit")
@UiDescriptor("feature-info-edit.xml")
@EditedEntityContainer("featureInfoDc")
@LoadDataBeforeShow
public class FeatureInfoEdit extends StandardEditor<FeatureInfo> {
    @Inject
    private TextField<String> pyModuleNameField;
    @Inject
    private TextArea<String> sqlQueryField;

    @Subscribe
    public void onAfterInit(AfterInitEvent event) {
        pyModuleNameField.addValidator(validateSqlQueryAndPyModuleName());
        sqlQueryField.addValidator(validateSqlQueryAndPyModuleName());
    }

    private Consumer<String> validateSqlQueryAndPyModuleName() {
        return value -> {
            if(sqlQueryField.getValue() != null && pyModuleNameField.getValue() != null) {
                throw new ValidationException("Entweder sqlQuery oder pyModuleName muss Null sein");
            }
        };
    }
}