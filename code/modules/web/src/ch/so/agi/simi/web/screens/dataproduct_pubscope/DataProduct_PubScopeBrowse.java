package ch.so.agi.simi.web.screens.dataproduct_pubscope;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.DataProduct_PubScope;

@UiController("simiProduct_DataProduct_PubScope.browse")
@UiDescriptor("data-product_pub-scope-browse.xml")
@LookupComponent("dataProduct_PubScopesTable")
@LoadDataBeforeShow
public class DataProduct_PubScopeBrowse extends StandardLookup<DataProduct_PubScope> {
}