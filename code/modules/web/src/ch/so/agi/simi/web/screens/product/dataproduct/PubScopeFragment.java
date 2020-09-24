package ch.so.agi.simi.web.screens.product.dataproduct;

import ch.so.agi.simi.entity.DataProduct_PubScope;
import ch.so.agi.simi.entity.product.DataProduct;
import com.haulmont.cuba.core.global.queryconditions.JpqlCondition;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("simiProduct_PubScopeFragment")
@UiDescriptor("pub-scope-fragment.xml")
public class PubScopeFragment extends ScreenFragment {
    @Inject
    private CollectionLoader<DataProduct_PubScope> pubScopesDl;

    private String filterProperty;

    public void setFilterProperty(String filterProperty) {
        this.filterProperty = filterProperty;
    }

    @Subscribe(target = Target.PARENT_CONTROLLER)
    public void onInitEntity(StandardEditor.InitEntityEvent<DataProduct> event) {
        DataProduct dataProduct = event.getEntity();
        pubScopesDl.getContainer().getItems().stream()
                .filter(DataProduct_PubScope::getDefaultValue)
                .findFirst()
                .ifPresent(dataProduct::setPubScope);
    }

    @Subscribe
    public void onAttach(AttachEvent event) {
        // load the available pubScope options
        pubScopesDl.setCondition(new JpqlCondition("e." + filterProperty + " = true"));
        pubScopesDl.load();
    }
}