package ch.so.agi.simi.web.screens.data.raster.rasterds;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.RasterDS;

@UiController("simiData_RasterDS.edit")
@UiDescriptor("raster-ds-edit.xml")
@EditedEntityContainer("rasterDSDc")
@LoadDataBeforeShow
public class RasterDSEdit extends StandardEditor<RasterDS> {
}