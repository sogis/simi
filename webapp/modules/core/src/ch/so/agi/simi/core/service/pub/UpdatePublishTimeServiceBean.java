package ch.so.agi.simi.core.service.pub;

import ch.so.agi.simi.core.service.pub.request.PubNotification;
import ch.so.agi.simi.core.service.pub.request.Region;
import ch.so.agi.simi.entity.theme.Theme;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.entity.theme.subarea.PublishedSubArea;
import ch.so.agi.simi.entity.theme.subarea.SubArea;
import ch.so.agi.simi.global.exc.CodedException;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
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

    public void update(String jsonMessage) throws CodedException {
        if (jsonMessage == null || jsonMessage.length() == 0)
            throw new CodedException(400, UpdatePublishTimeService.ERR_MSGBODY_EMPTY);

        PubNotification request = PubNotification.parseFromJson(jsonMessage);

        List<String> requestedSubAreaIdents = deferSubAreasFromRequest(request);

        Theme theme = loadTheme(request.getDataIdent());
        ThemePublication themePub = loadThemePub(request.getDataIdent(), theme);

        Map<String, PublishedSubArea> mapOfExistingPubSubs = loadExistingIntoKeyValMap(themePub, requestedSubAreaIdents);

        execDbInsertsAndUpdates(themePub, requestedSubAreaIdents, mapOfExistingPubSubs, request.getPublished());
    }

    private void execDbInsertsAndUpdates(
            ThemePublication themePub,
            List<String> requestedSubAreaIdents,
            Map<String, PublishedSubArea> mapOfExistingPubSubs,
            LocalDateTime published) {
        
        CommitContext context = new CommitContext();
        
        Map<String, SubArea> themePub_AllSubAreas = loadSubAreasIntoMap(themePub.getCoverageIdent());

        for(String requestedSubArea : requestedSubAreaIdents){
            if(mapOfExistingPubSubs.containsKey(requestedSubArea)){ //sql updates
                PublishedSubArea existing = mapOfExistingPubSubs.get(requestedSubArea);
                
                existing.setPrevPublished(existing.getPublished());
                existing.setPublished(published);
                
                context.addInstanceToCommit(existing);
            }
            else{ // sql inserts
                PublishedSubArea newPSub = new PublishedSubArea();
                newPSub.setPublished(published);
                newPSub.setPrevPublished(published);
                newPSub.setThemePublication(themePub);
                newPSub.setSubArea(themePub_AllSubAreas.get(requestedSubArea));

                context.addInstanceToCommit(newPSub);
            }
        }

        dataManager.commit(context);
    }

    private Map<String, SubArea> loadSubAreasIntoMap(String coverageIdent) {
        String query = MessageFormat.format("select e from {0} e where e.coverageIdent = :coverageIdent", SubArea.NAME);

        List<SubArea> subAreaList = dataManager.load(SubArea.class)
                .query(query)
                .parameter("coverageIdent", coverageIdent)
                .view("_minimal")
                .list();

        Map<String, SubArea> mapOfExisting = subAreaList.stream().collect(Collectors.toMap(SubArea::getIdentifier, Function.identity()));

        return mapOfExisting;
    }

    private List<String> deferSubAreasFromRequest(PubNotification request) {
        if(request.getPublishedRegions() == null || request.getPublishedRegions().size() == 0)
            return List.of(SubArea.KTSO_DEFAULT_IDENTIFIER);

        return request.getPublishedRegions().stream().map(Region::getRegion).collect(Collectors.toList());
    }


    private Theme loadTheme(String themePubIdentifier){
        Theme res = null;

        String query = MessageFormat.format("select e from {0} e where e.identifier = :identifier", Theme.NAME);

        Optional<Theme> themeWithThemePubIdentifier = dataManager.load(Theme.class)
                .query(query)
                .parameter("identifier", themePubIdentifier)
                .optional();

        if(themeWithThemePubIdentifier.isPresent()){
            res = themeWithThemePubIdentifier.get();
            log.debug("Found theme with identifier {} (same as themepub identifier)", themePubIdentifier);
        }
        else{ // query with omitted suffix
            List<String> identParts = Arrays.asList(themePubIdentifier.split("."));
            identParts.remove(identParts.size()-1);
            String themeIdent = String.join(".", identParts);

            Optional<Theme> themeWithShortenedIdentifier = dataManager.load(Theme.class)
                    .query(query)
                    .parameter("identifier", themeIdent)
                    .optional();

            log.debug("Query with shortened identifier {} yielded {}", themeIdent, themeWithShortenedIdentifier.orElse(null));

            res = themeWithShortenedIdentifier.orElse(null);
        }

        if(res == null)
            throw new CodedException(402, UpdatePublishTimeService.ERR_THEMEPUB_UNKNOWN, "Could not find theme for given themepub identifier");

        return res;
    }

    private ThemePublication loadThemePub(String themePubIdentifier, Theme parent){

        ThemePublication res = null;

        if(themePubIdentifier.equals(parent.getIdentifier())){
            String query = MessageFormat.format("select e from {0} e where e.theme = :theme", ThemePublication.NAME);

            Optional<ThemePublication> resWithThemeIdent = dataManager.load(ThemePublication.class)
                    .query(query)
                    .parameter("theme", parent)
                    .optional();

            log.debug("Load with parent {} and no suffix yielded {}", parent.getIdentifier(), resWithThemeIdent.orElse(null));

            res = resWithThemeIdent.orElse(null);
        }
        else{
            String identSuffix = null;

            String query = MessageFormat.format("select e from {0} e where e.theme = :theme and e.classSuffixOverride = :identSuffix", ThemePublication.NAME);

            Optional<ThemePublication> resWithSuffix = dataManager.load(ThemePublication.class)
                    .query(query)
                    .parameter("theme", parent)
                    .parameter("identSuffix", identSuffix)
                    .optional();

            log.debug("Load with parent {} and suffix {} yielded {}", parent.getIdentifier(), identSuffix, resWithSuffix.orElse(null));

            res = resWithSuffix.orElse(null);
        }

        if(res == null)
            throw new CodedException(404, UpdatePublishTimeService.ERR_THEMEPUB_UNKNOWN,
                    MessageFormat.format("Could not find themepublication {0} in db", themePubIdentifier));

        return res;
    }

    private Map<String, PublishedSubArea> loadExistingIntoKeyValMap(ThemePublication themePub, List<String> requestedRegionIdents){
        List<String> regionIdents = requestedRegionIdents;
        if(regionIdents == null || regionIdents.size() == 0){
            regionIdents = new LinkedList<String>();
            regionIdents.add(SubArea.KTSO_DEFAULT_IDENTIFIER); // Requests only submits regions if the theme is partitioned in > 1 parts.
        }

        List<PublishedSubArea> existing = loadExistingPSubs(themePub, regionIdents);

        Map<String, PublishedSubArea> mapOfExisting = existing.stream().collect(Collectors.toMap(PublishedSubArea::deferSubAreaIdent, Function.identity()));

        return mapOfExisting;
    }

    private List<PublishedSubArea> loadExistingPSubs(ThemePublication themePub, List<String> subAreaIdents){

        String rawQuery = "select e from {0} e " +
                "where e.themePublication = :themePublication " +
                "and e.subArea.coverageIdent = :coverageIdent" +
                "and e.subArea.identifier in (:subAreaIdentifiers)";

        String query = MessageFormat.format(rawQuery, PublishedSubArea.NAME);

        List<PublishedSubArea> existing = dataManager.load(PublishedSubArea.class)
                .query(query)
                .parameter("themePublication", themePub)
                .parameter("coverageIdent", themePub.getCoverageIdent())
                .parameter("subAreaIdentifiers", subAreaIdents)
                .list();

        return existing;
    }
}