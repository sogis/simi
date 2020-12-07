package ch.so.agi.simi.web.beans.copy;

import ch.so.agi.simi.entity.product.LayerGroup;
import ch.so.agi.simi.entity.product.ProductList;
import ch.so.agi.simi.entity.product.PropertiesInList;
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

    private ProductList updateCandidate;
    //private UUID productListUuid;
    //private List<PropertiesInList> updatedList;
    private List<PropertiesInList> othersChildLayerLinks;

    @Inject
    private DataManager dataManager;

    /**
     * Updates the updateCandidate's child layers from the given other product lists.
     * </p>
     * If a child layer exists in the updateCandidate, all properties except the sorting
     * are updated from child of the other product list.
     * </p>
     * Child Layer in the other product lists that do not exist in the updateCandidate
     * are added as new child layers to the updateCandidate. The added child layers are
     * set to a higher sort index than the existing ones in the updateCandidate.
     * </p>
     * CAUTION: The used updateCandidate must have his child layers loaded, otherwise
     * duplicate child layer references will be added to the updateCandidate
     * @param updateCandidate The map or layergroup that is updated
     * @param otherLayerGroups The other layergroups whose child layers are appended to the updateCandidate.
     * @return The added PropertiesInList entry with the lowest sort index. Null if no entries where added.
     */
    public Optional<PropertiesInList> updateLayersFromOtherLists(ProductList updateCandidate, Iterator<LayerGroup> otherLayerGroups){
        if(updateCandidate == null)
            throw new IllegalArgumentException("updateCandidate must not be null and have a singleActors collection");

        if(updateCandidate.getSingleActors() == null)
            updateCandidate.setSingleActors(new LinkedList<PropertiesInList>());

        if(otherLayerGroups == null)
            throw new IllegalArgumentException("otherProdListIds must not be null");

        this.updateCandidate = updateCandidate;

        if(!isUnderTest())
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

        Hashtable<UUID, PropertiesInList> candidateChildren = hashByLayerUuid(updateCandidate.getSingleActors());

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

        int sortIndex = calcMaxSort(updateCandidate);

        boolean firstIteration = true;

        PropertiesInList[] otherPils = distinctNewChildLayers.values().toArray(PropertiesInList[]::new);
        Arrays.sort(otherPils, compareBySortIdx);

        for(PropertiesInList otherPil : otherPils){
            PropertiesInList newPil = new PropertiesInList();
            copyRelevantAttributes(otherPil, newPil);

            sortIndex += 10;
            newPil.setProductList(updateCandidate);
            newPil.setSort(sortIndex);

            updateCandidate.getSingleActors().add(newPil);

            if(firstIteration){
                addedWithLowestIndex = newPil;
                firstIteration = false;
            }
        }

        return addedWithLowestIndex;
    }

    private static int calcMaxSort(ProductList updateCandidate) {
        int maxSort = 0;

        for(PropertiesInList pil : updateCandidate.getSingleActors()){

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
        toPil.setProductList(updateCandidate);
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

    void setOthersChildLayerLinks(List<PropertiesInList> othersChildLayerLinks){
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