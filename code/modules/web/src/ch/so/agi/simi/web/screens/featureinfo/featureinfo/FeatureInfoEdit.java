package ch.so.agi.simi.web.screens.featureinfo.featureinfo;

import ch.so.agi.simi.entity.featureinfo.FeatureInfo;
import ch.so.agi.simi.entity.featureinfo.LayerRelation;
import ch.so.agi.simi.entity.featureinfo.LayerRelationEnum;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.function.Consumer;

@UiController("simiFeatureInfo_FeatureInfo.edit")
@UiDescriptor("feature-info-edit.xml")
@EditedEntityContainer("featureInfoDc")
@LoadDataBeforeShow
public class FeatureInfoEdit extends StandardEditor<FeatureInfo> {
    @Inject
    private DataContext dataContext;
    @Inject
    private CollectionPropertyContainer<LayerRelation> layerRelationDc;
    @Inject
    private Notifications notifications;
    @Inject
    private TextField<String> pyModuleNameField;
    @Inject
    private TextArea<String> sqlQueryField;

    @Subscribe("addLayerRelationBtn")
    public void onAddLayerRelationBtnClick(Button.ClickEvent event) {
        LayerRelation layerRelation = dataContext.create(LayerRelation.class);
        layerRelation.setRelation(LayerRelationEnum.QUERIES);
        layerRelation.setFeatureInfo(this.getEditedEntity());

        layerRelationDc.getMutableItems().add(layerRelation);
    }

    @Subscribe("commitAndCloseBtn")
    public void onCommitAndCloseBtnClick(Button.ClickEvent event) {
        if (this.getEditedEntity().getLayerRelation().stream().filter(lr -> lr.getRelation() == LayerRelationEnum.IS_FOR_LAYER).count() == 1) {
            closeWithCommit();
        } else {
            notifications.create().withCaption("Es muss genau eine LayerRelation mit 'is for layer' haben").show();
        }
    }

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