package ch.so.agi.simi.web.screens.theme.themepub;

import ch.so.agi.simi.entity.theme.dbview.ThemePubValidation;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.UUID;


@UiController("simiTheme_ThemePubValidation.edit")
@UiDescriptor("theme-pub-validation-edit.xml")
@EditedEntityContainer("themePubValidationDc")
public class ThemePubValidationEdit extends StandardEditor<ThemePubValidation> {

    private UUID themePubId;

    @Inject
    private InstanceLoader<ThemePubValidation> themePubValidationDl;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        if(themePubId == null)
            return;

        themePubValidationDl.setEntityId(themePubId);
        themePubValidationDl.load();
    }

    public void setThemePubId(UUID themePubId) {
        this.themePubId = themePubId;
    }
}