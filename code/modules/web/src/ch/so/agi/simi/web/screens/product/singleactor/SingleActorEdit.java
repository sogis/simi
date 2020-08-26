package ch.so.agi.simi.web.screens.product.singleactor;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.SingleActor;

@UiController("simi_SingleActor.edit")
@UiDescriptor("single-actor-edit.xml")
@EditedEntityContainer("singleActorDc")
@LoadDataBeforeShow
public class SingleActorEdit extends StandardEditor<SingleActor> {
}