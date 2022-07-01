package ch.so.agi.simi.web.screens.data.datatheme;

import ch.so.agi.simi.entity.data.DbSchema;
import com.haulmont.cuba.gui.screen.*;

/*
import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.web.beans.datatheme.SchemaReaderClientBean;
import ch.so.agi.simi.web.beans.datatheme.ThemeReaderBean;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ContentMode;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;


import javax.inject.Inject;

 */

@UiController("simiData_DataTheme.edit")
@UiDescriptor("data-theme-edit.xml")
@EditedEntityContainer("modelSchemaDc")
@LoadDataBeforeShow
public class DataThemeEdit extends StandardEditor<DbSchema> {
        /*
    @Inject
    private ThemeReaderBean bean;

    @Inject
    InstanceContainer<DataTheme> modelSchemaDc;

    @Inject
    CollectionPropertyContainer<PostgresTable> tablesDc;



    @Inject
    private SchemaReaderClientBean client;

    @Inject
    DataContext context;

    @Inject
    private Dialogs dialogs;


    @Subscribe("btnReadAll")
    public void onBtnReadAllClick(Button.ClickEvent event) {

        DataTheme editedTheme = modelSchemaDc.getItem();

        boolean hasSavedTableConfigs = editedTheme.getPostgresTables() != null && editedTheme.getPostgresTables().size() > 0;

        if(hasSavedTableConfigs){

            dialogs.createOptionDialog()
                    .withCaption("Einlesen bedingt leere Tabellen-Liste")
                    .withMessage("Bitte die registrierten Tabellen vor dem Einlesen aus Simi löschen<p>Ablauf:<br>1. Registrierte Tabellen löschen<br>2. Löschungen durch speichern persistieren<br>3. Tabellen des Themas einlesen")
                    .withContentMode(ContentMode.HTML)
                    .withActions(new DialogAction(DialogAction.Type.CANCEL))
                    .show();

            return;
        }

        bean.actualizeWithDbCat(client, editedTheme);

        if(editedTheme.getPostgresTables() != null) {
            tablesDc.getMutableItems().addAll(editedTheme.getPostgresTables());
            context.merge(editedTheme.getPostgresTables());
        }
    }
     */
}