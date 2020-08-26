package ch.so.agi.simi.web.screens.product.dataproduct;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.DataProduct;

@UiController("simi_DataProduct.browse")
@UiDescriptor("data-product-browse.xml")
@LookupComponent("dataProductsTable")
@LoadDataBeforeShow
public class DataProductBrowse extends StandardLookup<DataProduct> {
}