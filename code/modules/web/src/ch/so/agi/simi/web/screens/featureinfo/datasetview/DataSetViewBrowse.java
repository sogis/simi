package ch.so.agi.simi.web.screens.featureinfo.datasetview;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.featureinfo.DataSetView;

@UiController("product_DataSetView.browse")
@UiDescriptor("data-set-view-browse.xml")
@LookupComponent("dataSetViewsTable")
@LoadDataBeforeShow
public class DataSetViewBrowse extends StandardLookup<DataSetView> {
}