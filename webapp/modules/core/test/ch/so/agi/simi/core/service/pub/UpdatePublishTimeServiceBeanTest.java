package ch.so.agi.simi.core.service.pub;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.entity.theme.Theme;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.entity.theme.ThemePublication_TypeEnum;
import ch.so.agi.simi.entity.theme.org.Agency;
import ch.so.agi.simi.entity.theme.subarea.PublishedSubArea;
import ch.so.agi.simi.entity.theme.subarea.SubArea;
import ch.so.agi.simi.global.exc.CodedException;
import ch.so.agi.simi.global.exc.SimiException;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains by intention no tests for linkToDefaultDataCoverage(...), as functionality is covered by
 * the update(...) method.
 */
class UpdatePublishTimeServiceBeanTest {

    public static final String IDENT_PREFIX = "inttest.publishsrv";
    public static final String THEME_THEMEPUB_SUFFIX = "suffix";

    public static Logger log = LoggerFactory.getLogger(UpdatePublishTimeServiceBeanTest.class);


    @RegisterExtension
    static TestContainer container = SimiTestContainer.Common.INSTANCE;

    static DataManager dataManager;
    static UpdatePublishTimeService serviceBean;

    @BeforeAll
    static void beforeAll() {
        dataManager = AppBeans.get(DataManager.class);
        serviceBean = AppBeans.get(UpdatePublishTimeService.class);
    }

    @BeforeEach
    void setUp() {
        deleteAllTestData();
    }

    @Test
    public void update_UnknowSubArea_ThrowsMatching(){

        String testIdent = String.join(".", IDENT_PREFIX, "UnknowSubArea");
        log.info("Starting test {}", testIdent);

        createThemePub(testIdent, false);
        //No subarea testdata generated -> subarea reference in json does not exist

        String json = buildTestJson(testIdent, 0, null);

        assertMatchingException(json, CodedException.ERR_SUBAREA_UNKNOWN);
    }

    @Test
    public void update_UnknowThemePub_ThrowsMatching(){

        String testIdent = String.join(".", IDENT_PREFIX, "UnknowThemePub");
        log.info("Starting test {}", testIdent);

        createSubAreas(testIdent, 0);
        //No themepublication testdata generated -> themepub reference in json does not exist

        String json = buildTestJson(testIdent, 0, null);

        assertMatchingException(json, CodedException.ERR_THEMEPUB_UNKNOWN);
    }

    private static void assertMatchingException(String jsonMessage, String exceptionMessage){
        RemoteException thrown = Assertions.assertThrows(RemoteException.class, () -> serviceBean.update(jsonMessage));
        RemoteException.Cause codingExcWrapper = thrown.getCause(CodedException.class.getName());

        if(codingExcWrapper == null)
            throw new SimiException("Thrown exception is not a instance of CodedException. Exception wrapper: {0}", thrown.toString());

        Assertions.assertEquals(exceptionMessage, codingExcWrapper.getMessage());
    }

    @Test
    public void update_NoRegion_NoSuffix_NoPsubs_OK(){

        String testIdent = String.join(".", IDENT_PREFIX, "NoRegion_NoSuffix_NoPsubs");
        log.info("Starting test {}", testIdent);

        createSubAreas(testIdent, 0);
        createThemePub(testIdent, false);

        String json = buildTestJson(testIdent, 0, null);

        assertInsertUpdate(1,0, serviceBean.update(json));
    }

    @Test
    public void update_WithRegion_WithSuffix_WithPsubs_OK(){

        String testIdent = String.join(".", IDENT_PREFIX, "WithRegion_WithSuffix_WithPsubs");
        log.info("Starting test {}", testIdent);

        createSubAreas(testIdent, 2);
        createThemePub(testIdent, true);

        String json = buildTestJson(testIdent,2, null);

        serviceBean.update(json); // Inserts the pSubs as precondition for this test

        assertInsertUpdate(0,2, serviceBean.update(json));
    }

    private static void assertInsertUpdate(int insCount, int updateCount, PublishResult res){
        Assertions.assertEquals(insCount, res.getDbInsertCount(), "Test must yield 1 insert");
        Assertions.assertEquals(updateCount, res.getDbUpdateCount(), "Test must yield 0 updates");
    }

    private static String buildTestJson(String testIdent, int regionCount, LocalDateTime published){
        String res = null;

        String themePubIdent = String.join(".", testIdent, THEME_THEMEPUB_SUFFIX);

        if(published == null)
            published = LocalDateTime.now();

        String basketPart = null;

        if(regionCount > 0){
            String regionPart1 =  "'publishedRegions': [{}]";
            String regionPart2 = "{" +
                    "'region': '{}'," +
                    "'publishedBaskets': [{" +
                    "'model': 'SO_AGI_MOpublic_20201009'," +
                    "'topic': 'Bodenbedeckung'," +
                    "'basket': 'oltenBID'" +
                    "}]" +
                    "}";

            LinkedList<String> regList = new LinkedList<>();
            for(int i=0; i<regionCount; i++){
                regList.add(format(regionPart2, appendIndex(testIdent, i)));
            }
            String regions = String.join(",", regList);

            basketPart = format(regionPart1, regions);
        }
        else{
            basketPart = "'publishedBaskets': [{" +
                    "'model': 'SO_AGI_MOpublic_20201009'," +
                    "'topic': 'Bodenbedeckung'," +
                    "'basket': 'oltenBID'" +
                    "}]";
        }

        String bodyTemplate = " { " +
                "'dataIdent': '{}'," +
                "'published': '{}'," +
                "{}" +
                "}";

        res = format(bodyTemplate, themePubIdent, published.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), basketPart);
        res = res.replaceAll("\'", "\"");

        return res;
    }

    private static String format(String format,String...params){
        FormattingTuple ft = MessageFormatter.arrayFormat(format,params);
        String message = ft.getMessage();
        return message;
    }

    private static String appendIndex(String ident, int index){
        return String.join("_", ident, String.valueOf(index));
    }

    private ThemePublication createThemePub(String testIdent, boolean suffixOnThemePub){
        ThemePublication res = null;

        CommitContext context = new CommitContext();

        Agency a = container.metadata().create(Agency.class);
        a.setName(testIdent);
        a.setAbbreviation("inttest");
        a.setEmail("email");
        a.setPhone("phone");
        a.setUrl("url");
        context.addInstanceToCommit(a);

        String themeIdentifier = testIdent;
        if(!suffixOnThemePub)
            themeIdentifier = String.join(".", testIdent, THEME_THEMEPUB_SUFFIX);

        Theme t = container.metadata().create(Theme.class);
        t.setIdentifier(themeIdentifier);
        t.setTitle(themeIdentifier);
        t.setDescription("desc");
        t.setDataOwner(a);
        context.addInstanceToCommit(t);


        String themePubSuffix = null;
        if(suffixOnThemePub)
            themePubSuffix = THEME_THEMEPUB_SUFFIX;

        ThemePublication p = container.metadata().create(ThemePublication.class);
        p.setDataClass(ThemePublication_TypeEnum.TABLE_SIMPLE);
        p.setTheme(t);
        p.setCoverageIdent(testIdent);
        p.setClassSuffixOverride(themePubSuffix);
        context.addInstanceToCommit(p);
        res = p;

        dataManager.commit(context);
        return res;
    }

    private LinkedList<SubArea> createSubAreas(String testIdent, int regionCountInRequest){

        LinkedList<SubArea> res = new LinkedList<>();

        CommitContext context = new CommitContext();

        int count = regionCountInRequest;
        if(regionCountInRequest == 0)
            count = 1; // Link to whole canton

        for(int i=0; i<count; i++){
            String countedIdent = appendIndex(testIdent, i);

            SubArea sub = container.metadata().create(SubArea.class);
            sub.setCoverageIdent(testIdent);
            sub.setGeomWKB(IDENT_PREFIX.getBytes());

            if(regionCountInRequest == 0)
                sub.setIdentifier(SubArea.KTSO_SUBAREA_IDENTIFIER);
            else
                sub.setIdentifier(countedIdent);

            context.addInstanceToCommit(sub);
            res.add(sub);
        }

        dataManager.commit(context);
        return res;
    }

    private void deleteAllTestData(){

        CommitContext context = new CommitContext();

        String pSubClause = "e.subArea.identifier";
        loadToDeleteContext(context, pSubClause, PublishedSubArea.class, PublishedSubArea.NAME);

        String subClause = "e.coverageIdent";
        loadToDeleteContext(context, subClause, SubArea.class, SubArea.NAME);

        String tpClause = "e.theme.identifier";
        loadToDeleteContext(context, tpClause, ThemePublication.class, ThemePublication.NAME);

        String themeClause = "e.identifier";
        loadToDeleteContext(context, themeClause, Theme.class, Theme.NAME);

        String agencyClause = "e.name";
        loadToDeleteContext(context, agencyClause, Agency.class, Agency.NAME);

        dataManager.commit(context);
    }

    private void loadToDeleteContext(CommitContext deleteContext, String identClausePart, Class entityClass, String entityName){
        String query = MessageFormat.format("select e from {0} e where {1} like :identifier", entityName, identClausePart);
        List<BaseUuidEntity> el = (List<BaseUuidEntity>)dataManager.load(entityClass)
                .query(query)
                .parameter("identifier", IDENT_PREFIX + "%")
                .view("_minimal")
                .list();

        for(BaseUuidEntity e : el)
            deleteContext.addInstanceToRemove(e);
    }
}