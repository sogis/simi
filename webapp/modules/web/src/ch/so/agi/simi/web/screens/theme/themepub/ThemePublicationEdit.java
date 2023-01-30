package ch.so.agi.simi.web.screens.theme.themepub;

import ch.so.agi.simi.entity.product.DataProduct;
import ch.so.agi.simi.entity.product.DataProductDsv;
import ch.so.agi.simi.entity.theme.Theme;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.entity.theme.ThemePublication_TypeEnum;
import ch.so.agi.simi.entity.theme.subarea.PublishedSubArea;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@UiController("simiTheme_ThemePublication.edit")
@UiDescriptor("theme-publication-edit.xml")
@EditedEntityContainer("themePublicationDc")
@LoadDataBeforeShow
public class ThemePublicationEdit extends StandardEditor<ThemePublication> {

    @Inject
    private CollectionContainer<DataProductDsv> deepProductsDc;

    @Inject
    private CollectionContainer<PublishedSubArea> pubAreasDc;

    @Inject
    private DataComponents dataComponents;

    @Inject
    private Notifications notifications;


    @Subscribe(id = "themePublicationDl", target = Target.DATA_LOADER)
    public void onThemePublicationDlPostLoad(InstanceLoader.PostLoadEvent event) {
        Entity e = event.getLoadedEntity();
        if(e == null)
            return;

        UUID loadedTpId = ((BaseUuidEntity)e).getId();

        loadProducts(loadedTpId);
        loadPublishedSubAreas(loadedTpId);
    }

    private void loadProducts(UUID themePubId){
        CollectionLoader<DataProductDsv> loader = dataComponents.createCollectionLoader();
        loader.setQuery("select e from simiProduct_DataProductDsv e where e.themePublication = :tpId");
        loader.setParameter("tpId", themePubId);

        loader.setContainer(deepProductsDc);
        loader.setDataContext(getScreenData().getDataContext());

        loader.load();
    }

    private void loadPublishedSubAreas(UUID themePubId){
        CollectionLoader<PublishedSubArea> loader = dataComponents.createCollectionLoader();
        loader.setQuery("select e from simiTheme_PublishedSubArea e where e.themePublication.id = :tpId");
        loader.setParameter("tpId", themePubId);

        loader.setContainer(pubAreasDc);
        loader.setDataContext(getScreenData().getDataContext());

        loader.setMaxResults(300);
        loader.load();

        if(loader.getContainer().getItems().size() == 200){
            notifications.create(Notifications.NotificationType.TRAY)
                    .withCaption("Teilladung publizierte Gebiete")
                    .withDescription("Es wurden nur die ersten 200 Zeilen geladen. Vollständige Liste siehe Themen-Verwaltung -> Gebiet-Verknüpfungen")
                    .show();
        }
    }

    @Inject
    TextField<String> identSuffixField;

    @Subscribe("dataClassField")
    public void onDataClassFieldValueChange(HasValue.ValueChangeEvent<ThemePublication_TypeEnum> event) {
        if(!event.isUserOriginated())
            return;

        String newSuffix = ThemePublication_TypeEnum.adaptSuffixToTypeChange(
                event.getPrevValue(),
                identSuffixField.getValue(),
                event.getValue());

        identSuffixField.setValue(newSuffix);
    }
}