package ch.so.agi.simi.core.service.pub;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.core.copy.CopyService;
import ch.so.agi.simi.core.service.pub.request.TestException;
import ch.so.agi.simi.core.test.Util;
import ch.so.agi.simi.entity.theme.ThemePublication;
import ch.so.agi.simi.global.exc.CodedException;
import ch.so.agi.simi.global.exc.SimiException;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.*;

class UpdatePublishTimeServiceBeanTest {

    @RegisterExtension
    static TestContainer container = SimiTestContainer.Common.INSTANCE;

    static DataManager dataManager;
    static UpdatePublishTimeService serviceBean;

    @BeforeAll
    static void beforeAll() {

        //dataManager = AppBeans.get(DataManager.class);
        serviceBean = AppBeans.get(UpdatePublishTimeService.class);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void update_NullRequestString_Throws() {
        assertMatchingException(null, UpdatePublishTimeService.ERR_MSGBODY_EMPTY);
    }

    @Test
    void update_EmptyRequestString_Throws() {
        assertMatchingException("", UpdatePublishTimeService.ERR_MSGBODY_EMPTY);
    }

    @Test
    void update_NonParseableJson_Throws() {
        assertMatchingException("{]", UpdatePublishTimeService.ERR_MSGBODY_INVALID);
    }

    @Test
    void update_JsonMissesMandatoryField_Throws() {
        String rawJson = "{" +
                    "'dataIdent': 'ch.so.afu.gewaesserschutz'," +
                    "'publishedBaskets': [{" +
                        "'model': 'SO_AGI_MOpublic_20201009'," +
                        "'topic': 'Bodenbedeckung'," +
                        "'basket': 'oltenBID'" +
                    "}]" +
                "}";

        String jsonMissingTimeStamp = rawJson.replaceAll("\'", "\"");

        assertMatchingException(jsonMissingTimeStamp, UpdatePublishTimeService.ERR_MSGBODY_INVALID);
    }

    @Test
    void fuu() {
        String rawJson = "{" +
                    "'dataIdent': 'ch.so.afu.gewaesserschutz'," +
                    "'published': 2021-12-23T14:54:49.050062," +
                    "'publishedBaskets': [{" +
                        "'model': 'SO_AGI_MOpublic_20201009'," +
                        "'topic': 'Bodenbedeckung'," +
                        "'basket': 'oltenBID'" +
                    "}]" +
                "}";

        String jsonWrongFieldType = rawJson.replaceAll("\'", "\"");

        assertMatchingException(jsonWrongFieldType, UpdatePublishTimeService.ERR_MSGBODY_INVALID);
    }

    @Test
    void update_JsonMissingBasketAndRegion_Throws() {
        String rawJson = "{" +
                    "'dataIdent': 'ch.so.afu.gewaesserschutz'," +
                    "'published': '2021-12-23T14:54:49.050062'," +
                "}";

        String jsonMissingBasketAndRegion = rawJson.replaceAll("\'", "\"");

        assertMatchingException(jsonMissingBasketAndRegion, UpdatePublishTimeService.ERR_MSGBODY_INVALID);
    }



    private static void assertMatchingException(String jsonMessage, String exceptionMessage){
        RemoteException thrown = Assertions.assertThrows(RemoteException.class, () -> serviceBean.update(jsonMessage));
        RemoteException.Cause codingExcWrapper = thrown.getCause(CodedException.class.getName());

        if(codingExcWrapper == null)
            throw new SimiException("Thrown exception is not a instance of CodedException. Exception wrapper: {0}", thrown.toString());

        Assertions.assertEquals(exceptionMessage, codingExcWrapper.getMessage());
    }

}