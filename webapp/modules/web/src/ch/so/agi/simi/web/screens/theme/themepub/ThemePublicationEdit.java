package ch.so.agi.simi.web.screens.theme.themepub;

import ch.so.agi.simi.entity.product.DataProduct;
import ch.so.agi.simi.entity.product.DataProductDsv;
import ch.so.agi.simi.entity.theme.Theme;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.entity.theme.ThemePublication_TypeEnum;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.UUID;

@UiController("simiTheme_ThemePublication.edit")
@UiDescriptor("theme-publication-edit.xml")
@EditedEntityContainer("themePublicationDc")
@LoadDataBeforeShow
public class ThemePublicationEdit extends StandardEditor<ThemePublication> {

    @Inject
    private CollectionContainer<DataProductDsv> deepProductsDc;

    @Inject
    private DataComponents dataComponents;

    @Subscribe(id = "themePublicationDl", target = Target.DATA_LOADER)
    public void onThemePublicationDlPostLoad(InstanceLoader.PostLoadEvent event) {
        Entity e = event.getLoadedEntity();
        if(e == null)
            return;

        ThemePublication loadedTp = (ThemePublication)e;

        CollectionLoader<DataProductDsv> loader = dataComponents.createCollectionLoader();
        loader.setQuery("select e from simiProduct_DataProductDsv e where e.themePublication = :tpId");
        loader.setParameter("tpId", loadedTp.getId());

        loader.setContainer(deepProductsDc);
        loader.setDataContext(getScreenData().getDataContext());

        loader.load();
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