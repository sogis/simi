package ch.so.agi.simi.web.screens.data.rasterds;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.RasterDS;

@UiController("simiData_RasterDS.browse")
@UiDescriptor("raster-ds-browse.xml")
@LookupComponent("rasterDsesTable")
@LoadDataBeforeShow
public class RasterDSBrowse extends StandardLookup<RasterDS> {
}