package ch.so.agi.simi.web.screens.dependency.relation;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.extended.Relation;

@UiController("simiExtended_Relation.edit")
@UiDescriptor("relation-edit.xml")
@EditedEntityContainer("relationDc")
@LoadDataBeforeShow
public class RelationEdit extends StandardEditor<Relation> {
}