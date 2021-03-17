package ch.so.agi.simi.web.screens.data.datasetviews.datasetview;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.data.datasetview.DataSetView;

@UiController("simiData_DataSetView.browse")
@UiDescriptor("data-set-view-browse.xml")
@LookupComponent("dataSetViewsTable")
@LoadDataBeforeShow
public class DataSetViewBrowse extends StandardLookup<DataSetView> {
}