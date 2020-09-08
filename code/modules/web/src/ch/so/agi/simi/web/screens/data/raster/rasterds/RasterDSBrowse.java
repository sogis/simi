package ch.so.agi.simi.web.screens.data.raster.rasterds;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.raster.RasterDS;

@UiController("simi_RasterDS.browse")
@UiDescriptor("raster-ds-browse.xml")
@LookupComponent("rasterDsesTable")
@LoadDataBeforeShow
public class RasterDSBrowse extends StandardLookup<RasterDS> {
}