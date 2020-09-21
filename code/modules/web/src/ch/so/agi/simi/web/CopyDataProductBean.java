package ch.so.agi.simi.web;

import ch.so.agi.simi.entity.data.raster.RasterView;
import ch.so.agi.simi.entity.data.tabular.TableView;
import ch.so.agi.simi.entity.product.*;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.MetadataTools;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Component(CopyDataProductBean.NAME)
public class CopyDataProductBean {
    public static final String NAME = "simi_CopyDataProductBean";
    @Inject
    private DataManager dataManager;
    @Inject
    private MetadataTools metadataTools;

    /**
     * Copy the DataProduct with entityId.
     *
     * @param entityClass The class of the original DataProduct.
     * @param entityId The Id of the original DataProduct.
     * @param <E> class of the DataProduct.
     */
    public <E extends DataProduct> void copyDataProduct(Class<E> entityClass, UUID entityId) {
        copyDataProduct(entityClass, entityId, null);
    }

    /**
     * Copy the DataProduct with entityId. All new entities are added to commitContext. If the commitContext is null
     * the copy of the DataProduct is committed.
     *
     * @param entityClass The class of the original DataProduct
     * @param entityId The Id of the original DataProduct
     * @param commitContext All copied entities are added to this commitContext
     * @param <E> class of the DataProduct.
     * @return A copy of the specified entity
     */
    private <E extends DataProduct> DataProduct copyDataProduct(Class<E> entityClass, UUID entityId, CommitContext commitContext) {
        DataProduct dataProduct;
        boolean commit = false;
        if (commitContext == null) {
            commitContext = new CommitContext();
            commit = true;
        }

        if (RasterView.class.equals(entityClass)) {
            dataProduct = copyDataProduct(RasterView.class, entityId, "copy-rasterView", commitContext, List.of(
                    RasterView::getPermissions,
                    RasterView::getFacadeLayers,
                    RasterView::getProductLists,
                    RasterView::getNotifyLayers,
                    RasterView::getLocatorLayers));

        } else if (TableView.class.equals(entityClass)) {
            dataProduct = copyDataProduct(TableView.class, entityId, "copy-tableView", commitContext, List.of(
                    TableView::getPermissions,
                    TableView::getFacadeLayers,
                    TableView::getProductLists,
                    TableView::getNotifyLayers,
                    TableView::getLocatorLayers,
                    TableView::getViewFields));

        } else if (FacadeLayer.class.equals(entityClass)) {
            FacadeLayer facadeLayer = copyDataProduct(FacadeLayer.class, entityId, "copy-facadeLayer", commitContext, List.of(FacadeLayer::getProductLists));
            dataProduct = facadeLayer;

            // handle referenced DataSetViews
            List<PropertiesInFacade> originalPropertiesInFacade = List.copyOf(facadeLayer.getDataSetViews());
            facadeLayer.getDataSetViews().clear();
            for (PropertiesInFacade originalPropertyInFacade : originalPropertiesInFacade) {
                // copy dataSetView
                DataSetView dataSetView = originalPropertyInFacade.getDataSetView();
                DataSetView dataSetViewCopy = (DataSetView) copyDataProduct(dataSetView.getClass(), dataSetView.getId(), commitContext);

                // change FacadeLayer references of dataSetViewCopy to copied instance
                for (PropertiesInFacade propertyInFacade : dataSetViewCopy.getFacadeLayers()) {
                    propertyInFacade.setFacadeLayer(facadeLayer);
                    facadeLayer.getDataSetViews().add(propertyInFacade);
                }
            }

        } else if (LayerGroup.class.equals(entityClass)) {
            LayerGroup layerGroup = copyDataProduct(LayerGroup.class, entityId, "copy-layerGroup", commitContext, List.of());
            dataProduct = layerGroup;

            // handle referenced SingleActors
            List<PropertiesInList> originalPropertiesInLists = List.copyOf(layerGroup.getSingleActors());
            layerGroup.getSingleActors().clear();
            for (PropertiesInList originalPropertyInList : originalPropertiesInLists) {
                // copy SingleActor
                SingleActor singleActor = originalPropertyInList.getSingleActor();
                SingleActor singleActorCopy = (SingleActor) copyDataProduct(singleActor.getClass(), singleActor.getId(), commitContext);

                // change LayerGroup references of singleActorCopy to copied instance
                for (PropertiesInList propertyInList : singleActorCopy.getProductLists()) {
                    propertyInList.setProductList(layerGroup);
                    layerGroup.getSingleActors().add(propertyInList);
                }
            }

        } else {
            throw new IllegalArgumentException(" Das Kopieren von DataProduct mit typ \"" + entityClass.getName() + "\" ist nicht unterstützt.");
        }

        if (commit) {
            dataManager.commit(commitContext);
        }
        return dataProduct;
    }

    /**
     * Copy the DataProduct with entityId. Including all referenced entity with a one to many relationship reachable by
     * the specified getters.
     *
     * @param entityClass The class of the original DataProduct
     * @param entityId The Id of the original DataProduct
     * @param view A view that loads all necessary properties
     * @param commitContext All copied entities are added to this commitContext
     * @param CopyListProperties Getter methods of one to many Properties that should be copied as well
     * @param <E> class of the DataProduct
     * @return A copy of the specified entity
     */
    private <E extends DataProduct> E copyDataProduct(Class<E> entityClass, UUID entityId, String view, CommitContext commitContext, List<Function<E, List<? extends StandardEntity>>> CopyListProperties) {
        E loadedEntity = dataManager.load(entityClass).id(entityId).view(view).one();
        E copy = metadataTools.deepCopy(loadedEntity);

        for (Function<E, List<? extends StandardEntity>> entities : CopyListProperties) {
            for (StandardEntity entity : entities.apply(copy)) {
                entity.setId(UUID.randomUUID());
                commitContext.addInstanceToCommit(entity);
            }
        }

        copy.setIdentifier(copy.getIdentifier() + "_kopie");
        copy.setId(UUID.randomUUID());
        commitContext.addInstanceToCommit(copy);

        return copy;
    }
}
