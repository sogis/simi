package ch.so.agi.simi.core.role;

import com.haulmont.cuba.security.app.role.AnnotatedRoleDefinition;
import com.haulmont.cuba.security.app.role.annotation.*;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.role.EntityAttributePermissionsContainer;
import com.haulmont.cuba.security.role.EntityPermissionsContainer;
import com.haulmont.cuba.security.role.ScreenComponentPermissionsContainer;
import com.haulmont.cuba.security.role.ScreenPermissionsContainer;

/**
 * Role for GIS-Coordinators
 *
 * The Role has full access to all entities.
 * Only Screens that are accessible from the DataProduct browser are allowed.
 * The ScreenComponentPermissions restrict some screen components from the default modify to view only.
 */
@Role(name = GISCoordinatorRole.NAME)
public class GISCoordinatorRole extends AnnotatedRoleDefinition {
    public final static String NAME = "GISCoordinator";

    // Allow all access to all entities
    @EntityAccess(entityName = "*", operations = {EntityOp.READ, EntityOp.CREATE, EntityOp.UPDATE, EntityOp.DELETE})
    @Override
    public EntityPermissionsContainer entityPermissions() {
        return super.entityPermissions();
    }

    // Allow modify access to all attributes of the entities
    @EntityAttributeAccess(entityName = "*", modify = "*")
    @Override
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return super.entityAttributePermissions();
    }

    // Access to DataProduct Screens
    @ScreenAccess(screenIds = {
            "simiProduct_DataProduct.browse",
            "simiProduct_DataSetView.browse",
            "simiProduct_SingleActor.browse",
            "simiData_PostgresTable.browse",
            "simiData_RasterDS.browse",

            "simiProduct_FacadeLayer.edit",
            "simiProduct_LayerGroup.edit",
            "simiProduct_Map.edit",
            "simiData_TableView.edit",
            "simiData_ViewField.edit",
            "simiData_RasterView.edit",
    })
    // Settings screen with "Change Password"
    @ScreenAccess(screenIds = {"settings"})
    @Override
    public ScreenPermissionsContainer screenPermissions() {
        return super.screenPermissions();
    }

    // Restrict access to certain components on screens
    @ScreenComponentAccess(screenId = "simiProduct_Map.edit", view = {
            "pubScopeFragment",
            "releasedAtField",
            "releasedThroughField",
            "btnAddSingleActor",
            "btnAddSingleActorsFromLayerGroup",
            "btnExclude",
    })
    @ScreenComponentAccess(screenId = "simiProduct_LayerGroup.edit", view = {
            "pubScopeFragment",
            "releasedAtField",
            "releasedThroughField",
            "btnAddSingleActor",
            "btnConvertToFacadeLayer",
            "btnExclude",
    })
    @Override
    public ScreenComponentPermissionsContainer screenComponentPermissions() {
        return super.screenComponentPermissions();
    }
}
