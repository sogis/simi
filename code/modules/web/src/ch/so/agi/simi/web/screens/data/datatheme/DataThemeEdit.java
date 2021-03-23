package ch.so.agi.simi.web.screens.data.datatheme;

import ch.so.agi.simi.entity.data.DataTheme;
import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.web.beans.datatheme.SchemaReaderClientBean;
import ch.so.agi.simi.web.beans.datatheme.ThemeReaderBean;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import jdk.nashorn.internal.ir.annotations.Immutable;

import javax.inject.Inject;

@UiController("simiData_DataTheme.edit")
@UiDescriptor("data-theme-edit.xml")
@EditedEntityContainer("modelSchemaDc")
@LoadDataBeforeShow
public class DataThemeEdit extends StandardEditor<DataTheme> {

    @Inject
    private ThemeReaderBean bean;

    @Inject
    InstanceContainer<DataTheme> modelSchemaDc;

    @Inject
    CollectionPropertyContainer<PostgresTable> tablesDc;

    @Inject
    private SchemaReaderClientBean client;

    @Subscribe("btnUpdateAll")
    public void onBtnUpdateAllClick(Button.ClickEvent event) {

        DataTheme editedTheme = modelSchemaDc.getItem();

        if(editedTheme.getPostgresTables() != null)
            tablesDc.getMutableItems().removeAll(editedTheme.getPostgresTables());

        bean.actualizeWithDbCat(client, editedTheme);

        if(editedTheme.getPostgresTables() != null)
            tablesDc.getMutableItems().addAll(editedTheme.getPostgresTables());
    }
    
}