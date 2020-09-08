package ch.so.agi.simi.core.role;

import com.haulmont.cuba.security.app.role.AnnotatedRoleDefinition;
import com.haulmont.cuba.security.app.role.annotation.Role;
import com.haulmont.cuba.security.app.role.annotation.ScreenComponentAccess;
import com.haulmont.cuba.security.role.ScreenComponentPermissionsContainer;

@Role(name = GISCoordinatorRole.NAME)
public class GISCoordinatorRole extends AnnotatedRoleDefinition {
    public final static String NAME = "GISCoordinator";

    @ScreenComponentAccess(screenId = "simiProduct_Map.edit", view = {"pubScopeField", "releasedAtField", "releasedThroughField", "btnAddSingleActor", "btnAddSingleActorsFromLayerGroup", "btnExclude"})
    @ScreenComponentAccess(screenId = "simiProduct_LayerGroup.edit", view = {"pubScopeField", "releasedAtField", "releasedThroughField", "btnAddSingleActor", "btnAddSingleActorsFromLayerGroup", "btnExclude"})
    @Override
    public ScreenComponentPermissionsContainer screenComponentPermissions() {
        return super.screenComponentPermissions();
    }
}
