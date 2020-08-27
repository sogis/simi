package ch.so.agi.simi.web.screens.product.map;

import ch.so.agi.simi.entity.DataProduct_PubScope;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.Map;

import javax.inject.Inject;

@UiController("simi_Map.edit")
@UiDescriptor("map-edit.xml")
@EditedEntityContainer("mapDc")
@LoadDataBeforeShow
public class MapEdit extends StandardEditor<Map> {
    @Inject
    private CollectionLoader<DataProduct_PubScope> pubScopesDl;

    @Subscribe
    public void onInitEntity(InitEntityEvent<Map> event) {
        Map map = event.getEntity();
        pubScopesDl.load();
        pubScopesDl.getContainer().getItems().stream()
                .filter(DataProduct_PubScope::getDefaultValue)
                .findFirst()
                .ifPresent(map::setPubScope);
    }
}