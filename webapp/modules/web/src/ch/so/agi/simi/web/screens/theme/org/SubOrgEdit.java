package ch.so.agi.simi.web.screens.theme.org;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.org.SubOrg;

@UiController("simiTheme_SubOrg.edit")
@UiDescriptor("sub-org-edit.xml")
@EditedEntityContainer("subOrgDc")
@LoadDataBeforeShow
public class SubOrgEdit extends StandardEditor<SubOrg> {
}