package ch.so.agi.simi.web.screens.dependency_vm;

import ch.so.agi.simi.entity.SimiEntity;
import ch.so.agi.simi.entity.data.PostgresTable;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.*;

@UiController("simi_Dependency_VM.browse")
@UiDescriptor("dependency_vm-browse.xml")
@LookupComponent("dependency_VMsTable")
@LoadDataBeforeShow
public class Dependency_VMBrowse extends StandardLookup<Dependency_VM> {

    @Inject
    private CollectionContainer<Dependency_VM> dependency_VMsDc;

    @Inject
    private TreeTable<Dependency_VM> dependency_VMsTable;

    @Inject
    private ScreenBuilders screenBuilders;

    @Inject
    private DataManager manager;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {

        List<Dependency_VM> samples = List.of(
                new Dependency_VM(), //0 tbl
                new Dependency_VM(), //1 job -> tbl
                new Dependency_VM(), //2 report -> tbl
                new Dependency_VM(), //3 dsv -> tbl
                new Dependency_VM(), //4 map -> dsv
                new Dependency_VM(), //5 fl -> dsv
                new Dependency_VM() //6 map -> fl
        );

        Dependency_VM conf = null;

        conf = samples.get(0);
        conf.setIdentifier("schema.tabelle");
        conf.setDisplay(conf.getIdentifier() + " (Tabelle)");
        conf.setParent(null);

        conf = samples.get(1);
        conf.setIdentifier("ada_fundstellen_pub/build.gradle");
        conf.setDisplay(conf.getIdentifier() + " (Job)");
        conf.setParent(samples.get(0));
        conf.setRelationDescription("Tabellenname kommt in Job vor.");

        conf = samples.get(2);
        conf.setIdentifier("uuid");
        conf.setDisplay("Punktfundstellen (Report)");
        conf.setParent(samples.get(0));

        conf = samples.get(3);
        conf.setIdentifier("ch.so.ada.punktfundstellen");
        conf.setDisplay(conf.getIdentifier() + " (View)");
        conf.setParent(samples.get(0));

        conf = samples.get(4);
        conf.setIdentifier("fundstellen");
        conf.setDisplay(conf.getIdentifier() + " (Karte)");
        conf.setParent(samples.get(3));

        conf = samples.get(5);
        conf.setIdentifier("ch.so.ada.fundstellen");
        conf.setDisplay(conf.getIdentifier() + " (Fassade)");
        conf.setParent(samples.get(3));

        conf = samples.get(6);
        conf.setIdentifier("fundstellen");
        conf.setDisplay(conf.getIdentifier() + " (Karte)");
        conf.setParent(samples.get(5));

        dependency_VMsDc.getMutableItems().addAll(samples);

        dependency_VMsTable.expandAll();
    }


    @Subscribe("dependency_VMsTable.edit")
    public void dependency_VMsTableEdit(Action.ActionPerformedEvent event) {

        Dependency_VM selected = dependency_VMsTable.getSingleSelected();
        if(selected == null)// || selected.getEntityIdent() == null)
            return;

        /*
        PostgresTable selEntity = manager.load(PostgresTable.class)
                .id(UUID.fromString("3203521e-2b8d-438a-b1d7-8053f5b9ed06"))
                .one();

        editSelectedEntity(selEntity, selEntity.getClass());

         */
    }

    private void editSelectedEntity(BaseUuidEntity entity, Class entityConcreteClass) {
        screenBuilders.editor(entityConcreteClass, this)
                .editEntity(entity)
                .build()
                .show();
    }

    private void editSelectedEntity(PostgresTable entity) {
        // Geht das auch mit dynamic cast a la  castTo(class, object). castTo(PostgresTable.class, simiEntity)

        screenBuilders.editor(PostgresTable.class, this)
                .editEntity(entity)
                .build()
                .show();
    }


}