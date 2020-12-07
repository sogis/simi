package ch.so.agi.simi.web.beans.copy;

import ch.so.agi.simi.entity.product.ProductList;
import ch.so.agi.simi.entity.product.PropertiesInList;
import ch.so.agi.simi.entity.product.SingleActor;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateFromOtherListsBeanTest {

    private static final UUID[] DUMMY_ARRAY = new UUID[0];

    UpdateFromOtherListsBean bean = new UpdateFromOtherListsBean();

    @Test
    public void newLayersAreAdded(){

        ProductList updateCandidate = new ProductList();

        List<PropertiesInList> otherChildLayers = new LinkedList<>();
        otherChildLayers.add(buildPil(UUID.randomUUID(), 11));
        otherChildLayers.add(buildPil(UUID.randomUUID(), 21));

        bean.setOthersChildLayerLinks(otherChildLayers);
        bean.updateLayersFromOtherLists(updateCandidate, DUMMY_ARRAY);

        assertChildrenContained(updateCandidate, otherChildLayers);
    }

    @Test
    public void existingLayersAreUpdatedInPlace(){

        UUID saUuid = UUID.randomUUID();

        ProductList updateCandidate = new ProductList();
        List<PropertiesInList> candLayers = new LinkedList<>();
        updateCandidate.setSingleActors(candLayers);
        candLayers.add(buildPil(saUuid, 10));
        candLayers.add(buildPil(UUID.randomUUID(), 20));

        PropertiesInList transferLayer = buildPil(saUuid, 11);

        List<PropertiesInList> otherChildLayers = new LinkedList<>();
        otherChildLayers.add(transferLayer);
        otherChildLayers.add(buildPil(UUID.randomUUID(), 21));

        bean.setOthersChildLayerLinks(otherChildLayers);
        bean.updateLayersFromOtherLists(updateCandidate, DUMMY_ARRAY);

        assertChildrenContained(updateCandidate, transferLayer);
    }

    @Test
    public void duplicateChildLayersAddedJustOnce(){

        ProductList updateCandidate = new ProductList();

        UUID id = UUID.randomUUID();
        int trans = 11;

        List<PropertiesInList> otherChildLayers = new LinkedList<>();

        PropertiesInList link1 = buildPil(id, trans);
        PropertiesInList link2 = buildPil(id, trans);

        otherChildLayers.add(link1);
        otherChildLayers.add(link2);

        bean.setOthersChildLayerLinks(otherChildLayers);
        bean.updateLayersFromOtherLists(updateCandidate, DUMMY_ARRAY);

        assertChildrenContained(updateCandidate, link1);
        assertChildrenContained(updateCandidate, link2); //must not make a difference whether link1 or link2 is checked

        assertEquals(1, updateCandidate.getSingleActors().size(), "updateCandidate must have exactly one child layer link");
    }

    @Test
    public void addedChildLayerHasMaxSortIndex(){

        UUID saUuid = UUID.randomUUID();

        ProductList updateCandidate = new ProductList();
        List<PropertiesInList> candLayers = new LinkedList<>();
        PropertiesInList candPil = buildPil(UUID.randomUUID(), 10);
        candPil.setSort(5);
        candLayers.add(candPil);
        updateCandidate.setSingleActors(candLayers);

        List<PropertiesInList> otherChildLayers = new LinkedList<>();
        otherChildLayers.add(buildPil(saUuid, 11));

        bean.setOthersChildLayerLinks(otherChildLayers);
        bean.updateLayersFromOtherLists(updateCandidate, DUMMY_ARRAY);

        int sortIndex = -1;
        for(PropertiesInList pil : updateCandidate.getSingleActors()){
            if(saUuid.equals(pil.getSingleActor().getId()))
                sortIndex = pil.getSort();
        }

        assertEquals(15, sortIndex, "Sort index of added layer must be 5 + 10 = 15. 5: Sort Index of existing layer, 10: Interval");
    }

    @Test
    public void returnsAddedWithLowestIndex(){
                ProductList updateCandidate = new ProductList();

        List<PropertiesInList> otherChildLayers = new LinkedList<>();

        PropertiesInList sort20 = buildPil(UUID.randomUUID(), 11);
        sort20.setSort(20);

        PropertiesInList sort10 = buildPil(UUID.randomUUID(), 11);
        sort10.setSort(10);

        PropertiesInList sort30 = buildPil(UUID.randomUUID(), 11);
        sort30.setSort(30);

        otherChildLayers.add(sort20);
        otherChildLayers.add(sort10); // For testing by intent in the middle of the list
        otherChildLayers.add(sort30);

        bean.setOthersChildLayerLinks(otherChildLayers);
        Optional<PropertiesInList> lowestIndex = bean.updateLayersFromOtherLists(updateCandidate, DUMMY_ARRAY);

        assertEquals(
                sort10.getSingleActor().getId(),
                lowestIndex.orElseThrow().getSingleActor().getId(),
                "Returned Layerlink must point to child layer with expected id");
    }

    private static void assertChildrenContained(ProductList updateCandidate, PropertiesInList childLayer) {

        LinkedList<PropertiesInList> list = new LinkedList<>();
        list.add(childLayer);

        assertChildrenContained(updateCandidate, list);
    }


    private static void assertChildrenContained(ProductList updateCandidate, List<PropertiesInList> children) {

        for(PropertiesInList otherChild : children){
            boolean found = false;

            for(PropertiesInList candidateChild : updateCandidate.getSingleActors()){
                UUID candidateId = candidateChild.getSingleActor().getId();
                UUID otherId = otherChild.getSingleActor().getId();

                if(candidateId.equals(otherId)){
                    if(candidateChild.getTransparency().intValue() == otherChild.getTransparency().intValue())
                        found = true;
                }
            }

            assertTrue(found, MessageFormat.format(
                    "Child with transparency {0} not found",
                    otherChild.getTransparency())
            );
        }
    }


    private static PropertiesInList buildPil(UUID childLayerUuid, int trans){
        PropertiesInList pil = new PropertiesInList();
        SingleActor sa = new SingleActor();
        sa.setId(childLayerUuid);
        pil.setTransparency(trans);
        pil.setSingleActor(sa);

        pil.setVisible(true);
        pil.setSort(0);

        return pil;
    }

}
