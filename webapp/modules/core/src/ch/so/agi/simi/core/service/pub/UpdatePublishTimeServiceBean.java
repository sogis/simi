package ch.so.agi.simi.core.service.pub;

import ch.so.agi.simi.core.service.pub.request.PubNotification;
import ch.so.agi.simi.core.service.pub.request.Region;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.entity.theme.subarea.PublishedSubArea;
import ch.so.agi.simi.entity.theme.subarea.SubArea;
import ch.so.agi.simi.global.exc.CodedException;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service(UpdatePublishTimeService.NAME)
public class UpdatePublishTimeServiceBean implements UpdatePublishTimeService {

    private static Logger log = LoggerFactory.getLogger(UpdatePublishTimeServiceBean.class);

    @Inject
    private DataManager dataManager;

    public PublishResult update(String jsonMessage) throws CodedException {
        if (jsonMessage == null || jsonMessage.length() == 0)
            throw new CodedException(400, CodedException.ERR_MSGBODY_EMPTY);

        PublishResult res = null;

        PubNotification request = PubNotification.parseFromJson(jsonMessage);
        if(!request.deferModelName().isPresent()) { //request with no regions and no baskets -> update not possible
            log.info("Skipping publication as no regions and no baskets where submitted");
            return new PublishResult(request.getDataIdent(), 0, 0);
        }

        List<String> requestedSubAreaIdents = deferSubAreasFromRequest(request);

        ThemePublication themePub = new ThemePubLoader().byIdentifier(request.getDataIdent());

        assertMatchingModelNames(request, themePub);

        Map<String, PublishedSubArea> mapOfExistingPubSubs = loadExistingIntoKeyValMap(themePub, requestedSubAreaIdents);

        Pair<Integer, Integer> updates = execDbInsertsAndUpdates(themePub, requestedSubAreaIdents, mapOfExistingPubSubs, request.getPublished());
        res = new PublishResult(request.getDataIdent(), updates.getLeft(), updates.getRight());

        return res;
    }

    private void assertMatchingModelNames(PubNotification request, ThemePublication themePub){

        String modelInRequest = request.deferModelName().get();

        if(!modelInRequest.equals(themePub.getPublicModelName())) {
            throw new CodedException(
                    404,
                    CodedException.ERR_MODEL_MISMATCH,
                    MessageFormat.format("Model in request: {0}. In conf (simi): {1}", modelInRequest, themePub.getPublicModelName())
            );
        }
    }

    public PublishResult update(String tpIdentifier, String coverageIdentifier, LocalDateTime publishInstant) throws CodedException {
        PublishResult res = null;

        List<String> subAreaIdents = loadSubAreaIdentifier(coverageIdentifier);

        ThemePublication themePub = ThemePubLoader.byIdentifier(tpIdentifier);
        themePub.setCoverageIdent(coverageIdentifier); // Non persistent overwrite for linking to default coverage

        Map<String, PublishedSubArea> mapOfExistingPubSubs = loadExistingIntoKeyValMap(themePub, subAreaIdents);

        Pair<Integer, Integer> updates = execDbInsertsAndUpdates(themePub, subAreaIdents, mapOfExistingPubSubs, publishInstant);
        res = new PublishResult(tpIdentifier, updates.getLeft(), updates.getRight());

        return res;
    }

    private Pair<Integer, Integer> execDbInsertsAndUpdates(
            ThemePublication themePub,
            List<String> requestedSubAreaIdents,
            Map<String, PublishedSubArea> mapOfExistingPubSubs,
            LocalDateTime published) {

        CommitContext context = new CommitContext();

        Map<String, SubArea> themePub_AllSubAreas = loadSubAreasIntoMap(themePub.getCoverageIdent());

        int updateCount = 0;
        int insertCount = 0;

        for(String requestedSubArea : requestedSubAreaIdents){
            if(mapOfExistingPubSubs.containsKey(requestedSubArea)){ //sql updates
                PublishedSubArea existing = mapOfExistingPubSubs.get(requestedSubArea);

                existing.setPrevPublished(existing.getPublished());
                existing.setPublished(published);

                context.addInstanceToCommit(existing);
                updateCount++;
            }
            else{ // sql inserts
                PublishedSubArea newPSub = new PublishedSubArea();
                newPSub.setPublished(published);
                newPSub.setPrevPublished(published);
                newPSub.setThemePublication(themePub);

                SubArea sub = themePub_AllSubAreas.get(requestedSubArea);
                if(sub == null) // FK auf Subarea ist wegen Master Edit-DB nullable, muss aber gesetzt werden
                    throw new CodedException(500, CodedException.ERR_SUBAREA_UNKNOWN, MessageFormat.format("Requested subarea {0} not found.", requestedSubArea));

                newPSub.setSubArea(sub);

                context.addInstanceToCommit(newPSub);
                insertCount++;
            }
        }

        dataManager.commit(context);
        return Pair.of(insertCount, updateCount);
    }

    private List<String> loadSubAreaIdentifier(String coverageIdent) {

        List<SubArea> subAreaList = loadSubAreas(coverageIdent);
        List<String> identList = subAreaList.stream().map(subArea -> subArea.getIdentifier()).collect(Collectors.toList());

        return identList;
    }

    private Map<String, SubArea> loadSubAreasIntoMap(String coverageIdent) {

        List<SubArea> subAreaList = loadSubAreas(coverageIdent);
        Map<String, SubArea> mapOfExisting = subAreaList.stream().collect(Collectors.toMap(SubArea::getIdentifier, Function.identity()));

        return mapOfExisting;
    }

    private List<SubArea> loadSubAreas(String coverageIdent) {
        String query = MessageFormat.format("select e from {0} e where e.coverageIdent = :coverageIdent", SubArea.NAME);

        List<SubArea> subAreaList = dataManager.load(SubArea.class)
                .query(query)
                .parameter("coverageIdent", coverageIdent)
                .view("_minimal")
                .list();

        return subAreaList;
    }

    private List<String> deferSubAreasFromRequest(PubNotification request) {
        if(request.getPublishedRegions() == null || request.getPublishedRegions().size() == 0)
            return List.of(SubArea.KTSO_SUBAREA_IDENTIFIER);

        return request.getPublishedRegions().stream().map(Region::getRegion).collect(Collectors.toList());
    }

    private Map<String, PublishedSubArea> loadExistingIntoKeyValMap(ThemePublication themePub, List<String> requestedRegionIdents){
        List<String> regionIdents = requestedRegionIdents;
        if(regionIdents == null || regionIdents.size() == 0){
            regionIdents = new LinkedList<String>();
            regionIdents.add(SubArea.KTSO_SUBAREA_IDENTIFIER); // Requests only submits regions if the theme is partitioned in > 1 parts.
        }

        List<PublishedSubArea> existing = loadExistingPSubs(themePub, regionIdents);

        Map<String, PublishedSubArea> mapOfExisting = existing.stream().collect(Collectors.toMap(PublishedSubArea::deferSubAreaIdent, Function.identity()));

        return mapOfExisting;
    }

    private List<PublishedSubArea> loadExistingPSubs(ThemePublication themePub, List<String> subAreaIdents){

        String rawQuery = "select e from {0} e " +
                "where e.themePublication = :themePublication " +
                "and e.subArea.coverageIdent = :coverageIdent " +
                "and e.subArea.identifier in :subAreaIdentifiers";

        String query = MessageFormat.format(rawQuery, PublishedSubArea.NAME);

        List<PublishedSubArea> existing = dataManager.load(PublishedSubArea.class)
                .query(query)
                .parameter("themePublication", themePub)
                .parameter("coverageIdent", themePub.getCoverageIdent())
                .parameter("subAreaIdentifiers", subAreaIdents)
                .view("publishedSubArea-pubService")
                .list();

        return existing;
    }
}