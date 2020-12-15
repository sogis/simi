package ch.so.agi.simi.web.screens.dependency.relation;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.dependency.Relation;

@UiController("simiDependency_Relation.edit")
@UiDescriptor("relation-edit.xml")
@EditedEntityContainer("relationDc")
@LoadDataBeforeShow
public class RelationEdit extends StandardEditor<Relation> {
}