package ch.so.agi.simi.web.screens.theme.org;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.org.Agency;

@UiController("simiTheme_Agency.edit")
@UiDescriptor("agency-edit.xml")
@EditedEntityContainer("agencyDc")
@LoadDataBeforeShow
public class AgencyEdit extends StandardEditor<Agency> {
}