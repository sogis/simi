package ch.so.agi.simi.web.screens.data.datasetviews.rasterview;

import ch.so.agi.simi.entity.data.datasetview.RasterView;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("simiData_RasterView.edit")
@UiDescriptor("raster-view-edit.xml")
@EditedEntityContainer("dataProductDc")
@LoadDataBeforeShow
public class RasterViewEdit extends StandardEditor<RasterView> {

}