package ch.so.agi.simi.web.beans.copy;

import ch.so.agi.simi.entity.product.non_dsv.LayerGroup;
import ch.so.agi.simi.entity.product.non_dsv.ProductList;
import ch.so.agi.simi.entity.product.non_dsv.PropertiesInList;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;

/**
 * See main Method "updateLayersFromOtherLists"
 */
@Component(UpdateFromOtherListsBean.NAME)
public class UpdateFromOtherListsBean {
    public static final String NAME = "simi_UpdateFromOtherListsBean";
    private static final String VIEW_NAME = "updateFromOtherListsBean-view";

    //private ProductList updateCandidate;
    private UUID productListUuid;
    private List<PropertiesInList> candidateList;
    private List<PropertiesInList> othersChildLayerLinks;

    @Inject
    private DataManager dataManager;

    /**
     * Updates the "candidateList" from the given "otherLayerGroups".
     * </p>
     * If a child layer exists in "candidateList", all properties except the sorting
     * are updated from the child of "otherLayerGroups".
     * </p>
     * Child Layers in "otherLayerGroups" that do not exist in "candidateList"
     * are added as new child layers to "candidateList". The added child layers are
     * set to a higher sort index than the existing ones in "candidateList".
     * @param productListUuid The UUID of the owning Map or LayerGroup of the "candidateList"
     * @param candidateList The list of child layer links of the map of layergroup represented by "productListUuid"
     * @param otherLayerGroups The other layergroups whose child layers are appended to the candidateList.
     * @return
     */
    public Optional<PropertiesInList> updateLayersFromOtherLists(UUID productListUuid, List<PropertiesInList> candidateList, Iterator<LayerGroup> otherLayerGroups){
        if(productListUuid == null)
            throw new IllegalArgumentException("productListUuid must not be null");

        if(candidateList == null)
            throw new IllegalArgumentException("pilList must not be null");

        this.candidateList = candidateList;
        this.productListUuid = productListUuid;

        if(otherLayerGroups != null) //Is null in unit tests, they do not load from the db and use _setOthersChildLayerLinksForTest() instead.
            loadLayersFromOtherLists(otherLayerGroups);

        // 1. Update properties for contained layers
        Map<UUID, PropertiesInList> newLayers = updateExistingLayers();

        // 2. Append properties for new layers
        PropertiesInList addedWithLowestIndex = appendNewLayers(newLayers);

        return Optional.ofNullable(addedWithLowestIndex);
    }

    private void loadLayersFromOtherLists(Iterator<LayerGroup> iter)
    {
        List<UUID> prodList = new LinkedList<>();

        while (iter.hasNext())
            prodList.add(iter.next().getId());

        String loadQuery =
                "select pil from simiProduct_ProductList pl join pl.singleActors pil where pl in :prodList";

        List<PropertiesInList> list = dataManager
                .load(PropertiesInList.class)
                .query(loadQuery)
                .view(VIEW_NAME)
                .parameter("prodList", prodList)
                .list();

        this.othersChildLayerLinks = list;
    }

    private Map<UUID, PropertiesInList> updateExistingLayers() {

        Hashtable<UUID, PropertiesInList> distinctNewChildLayers = new Hashtable<>();

        Hashtable<UUID, PropertiesInList> candidateChildren = hashByLayerUuid(candidateList);

        for(PropertiesInList otherPil : othersChildLayerLinks){
            PropertiesInList candidatePil = candidateChildren.getOrDefault(otherPil.getSingleActor().getId(), null);

            if(candidatePil == null)
                distinctNewChildLayers.putIfAbsent(otherPil.getSingleActor().getId(), otherPil);
            else
                mapPropertiesExceptSortFromTo(otherPil, candidatePil);
        }

        return distinctNewChildLayers;
    }

    private PropertiesInList appendNewLayers(Map<UUID, PropertiesInList> distinctNewChildLayers) {

        PropertiesInList addedWithLowestIndex = null;

        int sortIndex = calcMaxSort(candidateList);

        boolean firstIteration = true;

        PropertiesInList[] otherPils = distinctNewChildLayers.values().toArray(PropertiesInList[]::new);
        Arrays.sort(otherPils, compareBySortIdx);

        for(PropertiesInList otherPil : otherPils){
            PropertiesInList newPil = new PropertiesInList();
            copyRelevantAttributes(otherPil, newPil);

            sortIndex += 10;

            ProductList pl = new ProductList();
            pl.setId(productListUuid);
            newPil.setProductList(pl);
            newPil.setSort(sortIndex);

            candidateList.add(newPil);

            if(firstIteration){
                addedWithLowestIndex = newPil;
                firstIteration = false;
            }
        }

        return addedWithLowestIndex;
    }

    private static int calcMaxSort(List<PropertiesInList> updatedList) {
        int maxSort = 0;

        for(PropertiesInList pil : updatedList){

            if(pil.getSort() != null) {
                int curSort = pil.getSort();

                if (curSort > maxSort)
                    maxSort = curSort;
            }
        }

        return maxSort;
    }

    private void mapPropertiesExceptSortFromTo(PropertiesInList fromPil, PropertiesInList toPil) {

        int sortIndex = fromPil.getSort();
        copyRelevantAttributes(fromPil, toPil);

        toPil.setSort(sortIndex);

        ProductList pl = new ProductList();
        pl.setId(productListUuid);
        toPil.setProductList(pl);
    }

    private static Hashtable<UUID, PropertiesInList> hashByLayerUuid(List<PropertiesInList> inOutMapLayers) {
        Hashtable<UUID, PropertiesInList> result = new Hashtable<>();

        for(PropertiesInList pil : inOutMapLayers){
            result.putIfAbsent(pil.getSingleActor().getId(), pil);
        }

        return result;
    }

    private static void copyRelevantAttributes(PropertiesInList fromPil, PropertiesInList toPil){
        // local properties
        toPil.setVisible(fromPil.getVisible().booleanValue());
        toPil.setSort(fromPil.getSort().intValue());
        toPil.setTransparency(fromPil.getTransparency().intValue());

        // reference to the single actor child layer
        toPil.setSingleActor(fromPil.getSingleActor());
    }

    void _setOthersChildLayerLinksForTest(List<PropertiesInList> othersChildLayerLinks){
        this.othersChildLayerLinks = othersChildLayerLinks;
    }

    private boolean isUnderTest(){
        return othersChildLayerLinks != null;
    }

    private Comparator<PropertiesInList> compareBySortIdx = new Comparator<PropertiesInList>() {
        @Override
        public int compare(PropertiesInList o1, PropertiesInList o2) {

            Integer i1 = 0;
            Integer i2 = 0;

            if(o1.getSort() != null)
                i1 = o1.getSort();

            if(o2.getSort() != null)
                i2 = o2.getSort();

            return i1.compareTo(i2);
        }
    };
}