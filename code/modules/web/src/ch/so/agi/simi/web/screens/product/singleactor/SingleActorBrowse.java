package ch.so.agi.simi.web.screens.product.singleactor;

import com.haulmont.cuba.gui.screen.*;
import ch.so.agi.simi.entity.product.SingleActor;

@UiController("simiProduct_SingleActor.browse")
@UiDescriptor("single-actor-browse.xml")
@LookupComponent("singleActorsTable")
@LoadDataBeforeShow
public class SingleActorBrowse extends StandardLookup<SingleActor> {
}