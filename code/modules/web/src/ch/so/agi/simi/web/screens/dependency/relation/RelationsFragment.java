package ch.so.agi.simi.web.screens.dependency.relation;

import ch.so.agi.simi.entity.dependency.Dependency;
import ch.so.agi.simi.entity.dependency.Relation;
import ch.so.agi.simi.entity.product.datasetview.DataSetView;
import ch.so.agi.simi.entity.product.non_dsv.PropertiesInFacade;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("simiDependency_RelationsFragment")
@UiDescriptor("relations-fragment.xml")
public class RelationsFragment extends ScreenFragment {
    @Inject
    private CollectionLoader<DataSetView> dataSetViewsDl;

    @Inject
    private DataContext dataContext;
    @Inject
    private CollectionPropertyContainer<Relation> relationsDc;
    @Inject
    private Table<Relation> relationsTable;


    @Subscribe(target = Target.PARENT_CONTROLLER)
    private void onBeforeShowHost(Screen.BeforeShowEvent event) {
        dataSetViewsDl.load();
    }

    @Subscribe("btnAddRelation")
    public void onBtnAddRelationClick(Button.ClickEvent event) {

        Relation r = dataContext.create(Relation.class);

        StandardEditor<Dependency> parentController = (StandardEditor<Dependency>)this.getHostController();
        r.setDependency(parentController.getEditedEntity());

        relationsDc.getMutableItems().add(r);
        relationsTable.requestFocus(r, "dataSetView");
    }
}