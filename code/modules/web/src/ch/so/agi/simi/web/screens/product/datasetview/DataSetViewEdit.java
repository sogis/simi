package ch.so.agi.simi.web.screens.product.datasetview;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.DataSetView;

@UiController("product_DataSetView.edit")
@UiDescriptor("data-set-view-edit.xml")
@EditedEntityContainer("dataSetViewDc")
@LoadDataBeforeShow
public class DataSetViewEdit extends StandardEditor<DataSetView> {
}