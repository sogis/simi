package ch.so.agi.simi.core.morph;

import ch.so.agi.simi.entity.data.datasetview.DataSetView;
import ch.so.agi.simi.entity.product.*;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service(MorphProductService.NAME)
public class MorphProductServiceBean implements MorphProductService {

    public static final String VIEWNAME_FACADE = "morph-FacadeLayer";
    public static final String VIEWNAME_GROUP = "morph-LayerGroup";
    public static final String VIEWNAME_MAP = "morph-Map";

    @Inject
    private DataManager dataManager;

    @Override
    public UUID morphProduct(UUID prodID, ProdType fromType, ProdType toType) {

        if(fromType == toType)
            return prodID;

        DataProduct toDp = null;
        try{
            DataProduct fromDp = loadWithView(prodID, fromType);

            assertValidInputType(fromDp);
            assertNoFlChildren(fromDp);

            toDp = createTargetDp(toType);

            copyDprodProperties(fromDp, toDp);
            copyChildLinks(fromDp, toDp);

            toDp.setDerivedIdentifier(fromDp.getDerivedIdentifier() + "-morph"); // Notwendig zur Vermeidung von UK verletzung.

            CommitContext trans = new CommitContext();
            trans.addInstanceToCommit(toDp);
            addNewLinksToCommit(toDp, trans);
            trans.addInstanceToRemove(fromDp);

            dataManager.commit(trans);

            updateIdentifier(toDp.getId(), fromDp.getIdentPart()); // Reset auf original identifier ohne "-morph"
        }
        catch(MorphException me){
            throw me;
        }
        catch(Exception e){
            throw new MorphException("Umwandlung aufgrund Fehler abgebrochen.", e);
        }

        return toDp.getId();
    }

    private static void addNewLinksToCommit(DataProduct dp, CommitContext trans){
        if(dp instanceof FacadeLayer){
            FacadeLayer fl = (FacadeLayer)dp;
            if (fl.getDataSetViews() == null)
                return;

            for(PropertiesInFacade pif : fl.getDataSetViews())
                trans.addInstanceToCommit(pif);
        }
        else if(dp instanceof ProductList){
            ProductList pl = (ProductList)dp;
            if (pl.getSingleActors() == null)
                return;

            for(PropertiesInList pil : pl.getSingleActors())
                trans.addInstanceToCommit(pil);
        }
    }

    private void updateIdentifier(UUID dataProdId, String identifier){
        DataProduct dProd = dataManager.load(DataProduct.class).id(dataProdId).one();
        dProd.setIdentPart(identifier);
        dataManager.commit(dProd);
    }

    private static void copyDprodProperties(DataProduct fromDp, DataProduct toDp) {
        toDp.setIdentPart(fromDp.getIdentPart());
        toDp.setIdentIsPartial(fromDp.getIdentIsPartial());
        toDp.setDerivedIdentifier(fromDp.getDerivedIdentifier());
        toDp.setPubScope(fromDp.getPubScope());
        toDp.set_keywords_deprecated(fromDp.get_keywords_deprecated());
        toDp.setRemarks(fromDp.getRemarks());
        toDp.set_synonyms_deprecated(fromDp.get_synonyms_deprecated());
        toDp.setTitle(fromDp.getTitle());
    }

    private void copyChildLinks(DataProduct fromDp, DataProduct toDp) {

        if (fromDp instanceof ProductList) {
            if (toDp instanceof FacadeLayer){
                copyChildren( (ProductList)fromDp, (FacadeLayer)toDp );
            }
            else if (toDp instanceof ProductList){
                copyChildren( (ProductList)fromDp, (ProductList)toDp );
            }
        }
        else if (fromDp instanceof FacadeLayer){
            if ( !(toDp instanceof ProductList) )
                throw new MorphException("Expecting target Type ProductList");

            copyChildren( (FacadeLayer)fromDp, (ProductList)toDp );
        }
        else {
            throw new MorphException("Nicht implementiert");
        }
    }

    private void copyChildren(ProductList fromPL, ProductList toPL){

        if(fromPL.getSingleActors() != null){

            LinkedList<PropertiesInList> pil = new LinkedList<>();

            for (PropertiesInList fromList : fromPL.getSingleActors()) {
                PropertiesInList toList = dataManager.create(PropertiesInList.class);
                // dataManager.remove(propertiesInFacade);

                toList.setSort(fromList.getSort());
                toList.setProductList(toPL);
                toList.setSingleActor(fromList.getSingleActor());

                pil.add(toList);
            }

            toPL.setSingleActors(pil);
        }
    }

    private void copyChildren(ProductList fromPL, FacadeLayer toFL){

        if(fromPL.getSingleActors() != null){

            LinkedList<PropertiesInFacade> pif = new LinkedList<>();

            for (PropertiesInList propertiesInList : fromPL.getSingleActors()) {
                PropertiesInFacade propertiesInFacade = dataManager.create(PropertiesInFacade.class);
                //dataManager.remove(propertiesInList);

                propertiesInFacade.setSort(propertiesInList.getSort());
                propertiesInFacade.setFacadeLayer(toFL);
                propertiesInFacade.setDataSetView( (DataSetView)propertiesInList.getSingleActor() );

                pif.add(propertiesInFacade);
            }

            toFL.setDataSetViews(pif);
        }
    }

    private void copyChildren(FacadeLayer fromFL, ProductList toPL){
        if (fromFL.getDataSetViews() != null) {

            LinkedList<PropertiesInList> pil = new LinkedList<>();

            for (PropertiesInFacade propertiesInFacade : fromFL.getDataSetViews()) {
                PropertiesInList propertiesInList = dataManager.create(PropertiesInList.class);
                // dataManager.remove(propertiesInFacade);

                propertiesInList.setSort(propertiesInFacade.getSort());
                propertiesInList.setProductList(toPL);
                propertiesInList.setSingleActor(propertiesInFacade.getDataSetView());

                pil.add(propertiesInList);
            }

            toPL.setSingleActors(pil);
        }
    }

    private DataProduct createTargetDp(ProdType toType) {

        DataProduct res = null;

        if(toType == ProdType.FACADELAYER) {
            res = dataManager.create(FacadeLayer.class);
        }
        else if(toType == ProdType.LAYERGROUP) {
            res = dataManager.create(LayerGroup.class);
        }
        else if(toType == ProdType.MAP) {
            res = dataManager.create(Map.class);
            ((Map)res).setBackground(false);
        }

        return res;
    }

    private DataProduct loadWithView(UUID prodID, ProdType fromType){

        DataProduct res = null;

        if(fromType == ProdType.FACADELAYER)
            res = dataManager.load(FacadeLayer.class).view(VIEWNAME_FACADE).id(prodID).one();
        else if(fromType == ProdType.LAYERGROUP)
            res = dataManager.load(LayerGroup.class).view(VIEWNAME_GROUP).id(prodID).one();
        else if(fromType == ProdType.MAP)
            res = dataManager.load(Map.class).view(VIEWNAME_MAP).id(prodID).one();

        return res;
    }

    private static void assertNoFlChildren(DataProduct fromDp) {
        if( !(fromDp instanceof ProductList) )
            return;

        List<PropertiesInList> pilList = ((ProductList)fromDp).getSingleActors();

        if (pilList != null && pilList.stream().anyMatch(pil -> !(pil.getSingleActor() instanceof DataSetView))) {
            throw new MorphException("ProduktListe kann nicht umgewandelt werden, da die Liste Facadelayer enthält");
        }
    }

    private static void assertValidInputType(DataProduct dp){

        boolean valid = false;

        if(dp != null)
            valid = (dp instanceof FacadeLayer || dp instanceof LayerGroup || dp instanceof Map);

        if(!valid)
            throw new MorphException("Umzuwandelndes Produkt muss ein FacadeLayer oder eine Produktliste (LayerGroup, Map) sein");
    }
}