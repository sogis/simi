package ch.so.agi.simi.web.screens.product.datasetview;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.DataSetView;

@UiController("simiProduct_DataSetView.browse")
@UiDescriptor("data-set-view-browse.xml")
@LookupComponent("dataSetViewsTable")
@LoadDataBeforeShow
public class DataSetViewBrowse extends StandardLookup<DataSetView> {
}