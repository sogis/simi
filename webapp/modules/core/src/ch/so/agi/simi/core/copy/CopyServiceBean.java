package ch.so.agi.simi.core.copy;

import ch.so.agi.simi.entity.extended.Relation;
import ch.so.agi.simi.entity.iam.Permission;
import ch.so.agi.simi.entity.data.datasetview.DataSetView;
import ch.so.agi.simi.entity.data.datasetview.RasterView;
import ch.so.agi.simi.entity.data.datasetview.TableView;
import ch.so.agi.simi.entity.data.datasetview.ViewField;
import ch.so.agi.simi.entity.product.*;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EntitySet;
import com.haulmont.cuba.core.global.MetadataTools;
import org.springframework.stereotype.Service;


import javax.inject.Inject;
import java.util.UUID;

@Service(CopyService.NAME)
public class CopyServiceBean implements CopyService {
    
    public static final String TV_VIEW_NAME = "copy-tableView";
    public static final String RV_VIEW_NAME = "copy-rasterView";
    public static final String FL_VIEW_NAME = "copy-facadeLayer";
    public static final String LG_VIEW_NAME = "copy-layerGroup";
    public static final String MAP_VIEW_NAME = "copy-map";

    @Inject
    private DataManager dataManager;
    @Inject
    private MetadataTools tools;

    @Override
    public UUID copyProduct(UUID id){

        DataProduct dp = dataManager.load(DataProduct.class).id(id).one();

        UUID copyId = null;
        if(dp instanceof TableView)
            copyId = copyTableView(dp.getId());
        else if(dp instanceof RasterView)
            copyId = copyRasterView(dp.getId());
        else if(dp instanceof FacadeLayer)
            copyId = copyFacadeLayer(dp.getId());
        else if(dp instanceof LayerGroup)
            copyId = copyLayerGroup(dp.getId());
        else if(dp instanceof Map)
            copyId = copyMap(dp.getId());
        else
            throw new RuntimeException("not implemented");

        return copyId;
    }

    private UUID copyFacadeLayer(UUID id){

        FacadeLayer fl = dataManager.load(FacadeLayer.class).id(id).view(FL_VIEW_NAME).one();
        FacadeLayer flCopy = tools.deepCopy(fl);

        EntitySet changedEntities = setNewIdentifiers(flCopy);

        dataManager.commit(new CommitContext(changedEntities));

        return flCopy.getId();
    }

    private UUID copyMap(UUID id){

        Map map = dataManager.load(Map.class).id(id).view(MAP_VIEW_NAME).one();
        Map mapCopy = tools.deepCopy(map);

        EntitySet changedEntities = setNewIdentifiers(mapCopy);

        dataManager.commit(new CommitContext(changedEntities));

        return mapCopy.getId();
    }

    private UUID copyLayerGroup(UUID id){

        LayerGroup lg = dataManager.load(LayerGroup.class).id(id).view(LG_VIEW_NAME).one();
        LayerGroup lgCopy = tools.deepCopy(lg);

        EntitySet changedEntities = setNewIdentifiers(lgCopy);

        dataManager.commit(new CommitContext(changedEntities));

        return lgCopy.getId();
    }

    private UUID copyTableView(UUID id){

        TableView tv = dataManager.load(TableView.class).id(id).view(TV_VIEW_NAME).one();
        TableView tvCopy = tools.deepCopy(tv);

        EntitySet changedEntities = setNewIdentifiers(tvCopy);

        dataManager.commit(new CommitContext(changedEntities));

        return tvCopy.getId();
    }

    private UUID copyRasterView(UUID id){

        RasterView rv = dataManager.load(RasterView.class).id(id).view(RV_VIEW_NAME).one();
        RasterView rvCopy = tools.deepCopy(rv);

        EntitySet changedEntities = setNewIdentifiers(rvCopy);

        dataManager.commit(new CommitContext(changedEntities));

        return rvCopy.getId();
    }

    private static EntitySet setNewIdentifiers(RasterView rv){

        EntitySet changed = new EntitySet();

        setLocalProps(rv, changed);
        
        setPermIds(rv, changed);
        
        setRelIds(rv, changed);

        return changed;
    }

    private static void setRelIds(DataSetView dsv, EntitySet inOutCommitList) {
        
        for(Relation r : dsv.getRelations()) {
            r.setId(UUID.randomUUID());
            inOutCommitList.add(r);
        }
    }

    private static void setPermIds(DataSetView dsv, EntitySet inOutCommitList) {
        for(Permission p : dsv.getPermissions()) {
            p.setId(UUID.randomUUID());
            inOutCommitList.add(p);
        }
    }

    private static EntitySet setNewIdentifiers(LayerGroup lg){

        EntitySet changed = new EntitySet();

        setLocalProps(lg, changed);
        setPilPsIds(lg, changed);

        return changed;
    }

    private static EntitySet setNewIdentifiers(Map map){

        EntitySet changed = new EntitySet();

        setLocalProps(map, changed);
        setPilPsIds(map, changed);

        return changed;
    }

    private static EntitySet setNewIdentifiers(FacadeLayer fl){

        EntitySet changed = new EntitySet();

        setLocalProps(fl, changed);
        setPifFlIds(fl, changed);

        return changed;
    }

    private static EntitySet setNewIdentifiers(TableView tv){

        EntitySet changed = new EntitySet();

        setLocalProps(tv, changed);

        setPermIds(tv, changed);

        setRelIds(tv, changed);

        for(ViewField vf : tv.getViewFields()) {
            vf.setId(UUID.randomUUID());
            changed.add(vf);
        }

        return changed;
    }

    private static void setLocalProps(DataProduct dp, EntitySet inOutCommitList){
        dp.setId(UUID.randomUUID());
        dp.setIdentifier(dp.getIdentifier() + "-kopie"); //Bindestrich, damit bei sortierter Anzeige nahe beim Original

        if(dp.getTitle() != null)
            dp.setTitle(dp.getTitle() + " - Kopie");

        inOutCommitList.add(dp);
    }

    private static void setPifFlIds(FacadeLayer fl, EntitySet inOutCommitList){

        for(PropertiesInFacade pif : fl.getDataSetViews()) {
            pif.setId(UUID.randomUUID());
            inOutCommitList.add(pif);
        }
    }

    private static void setPifDsvIds(DataSetView dsv, EntitySet inOutCommitList){

        for(PropertiesInFacade pif : dsv.getFacadeLayers()) {
            pif.setId(UUID.randomUUID());
            inOutCommitList.add(pif);
        }
    }

    private static void setPilPsIds(ProductList pl, EntitySet inOutCommitList){

        for(PropertiesInList pil : pl.getSingleActors()) {
            pil.setId(UUID.randomUUID());
            inOutCommitList.add(pil);
        }
    }

    private static void setPilSaIds(SingleActor sa, EntitySet inOutCommitList){

        for(PropertiesInList pil : sa.getProductLists()) {
            pil.setId(UUID.randomUUID());
            inOutCommitList.add(pil);
        }
    }

}