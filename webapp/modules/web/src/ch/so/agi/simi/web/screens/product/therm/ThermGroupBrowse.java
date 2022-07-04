package ch.so.agi.simi.web.screens.product.therm;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.ThermGroup;

@UiController("simiProduct_ThermGroup.browse")
@UiDescriptor("therm-group-browse.xml")
@LookupComponent("thermGroupsTable")
@LoadDataBeforeShow
public class ThermGroupBrowse extends StandardLookup<ThermGroup> {
}