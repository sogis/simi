package ch.so.agi.simi.web.screens.theme.org;

import ch.so.agi.simi.entity.product.DataProduct;
import ch.so.agi.simi.entity.theme.org.Agency;
import ch.so.agi.simi.entity.theme.org.SubOrg;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.theme.org.OrgUnit;

import javax.inject.Inject;

@UiController("simiTheme_OrgUnit.browse")
@UiDescriptor("org-unit-browse.xml")
@LookupComponent("orgUnitsTable")
@LoadDataBeforeShow
public class OrgUnitBrowse extends StandardLookup<OrgUnit> {

    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Table<OrgUnit> orgUnitsTable;
    @Inject
    private Metadata metadata;

    @Subscribe("createAgencyBtn")
    public void onCreateAgencyBtnClick(Button.ClickEvent event) {
        OrgUnit ou = metadata.create(Agency.class);
        showCreateEditorForOrgUnit(ou);
    }

    @Subscribe("createSubOrgBtn")
    public void onCreateSubOrgBtnClick(Button.ClickEvent event) {
        OrgUnit ou = metadata.create(SubOrg.class);
        showCreateEditorForOrgUnit(ou);
    }

    private void showCreateEditorForOrgUnit(OrgUnit orgUnit) {
        screenBuilders.editor(orgUnitsTable)
                .editEntity(orgUnit)
                .build()
                .show();
    }
}