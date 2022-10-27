package ch.so.agi.simi.core.service.pub.request;

import ch.so.agi.simi.core.service.pub.UpdatePublishTimeService;
import ch.so.agi.simi.global.exc.CodedException;
import ch.so.agi.simi.global.exc.SimiException;
import com.haulmont.cuba.core.global.RemoteException;
import org.aspectj.apache.bcel.classfile.Code;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class PubNotificationTest {

    private static Logger log = LoggerFactory.getLogger(PubNotificationTest.class);

    @Test
    void parse_Null_Throws() {
        assertMatchingException(null, UpdatePublishTimeService.ERR_MSGBODY_EMPTY);
    }

    @Test
    void parse_EmptyString_Throws() {
        assertMatchingException("", UpdatePublishTimeService.ERR_MSGBODY_EMPTY);
    }

    @Test
    void parse_NonParseableJson_Throws() {
        assertMatchingException("{]", UpdatePublishTimeService.ERR_MSGBODY_INVALID);
    }

    @Test
    void parse_JsonMissingMandatoryField_Throws() {
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
    void parse_JsonHavingWrongFieldType_Throws() {
        String rawJson = "{" +
                    "'dataIdent': 'ch.so.afu.gewaesserschutz'," +
                    "'published': 99," +
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
    void parse_SunnyJson_OK() {
        String rawJson = "{" +
                    "'dataIdent': 'ch.so.afu.gewaesserschutz'," +
                    "'published': '2021-12-23T14:54:49.050062'," +
                    "'publishedBaskets': [{" +
                        "'model': 'SO_AGI_MOpublic_20201009'," +
                        "'topic': 'Bodenbedeckung'," +
                        "'basket': 'oltenBID'" +
                    "}]" +
                "}";
        String sunnyJson = rawJson.replaceAll("\'", "\"");

        PubNotification.parseFromJson(sunnyJson);
    }

    @Test
    void parse_JsonMissingBasketAndRegion_Throws() {
        String rawJson = "{" +
                    "'dataIdent': 'ch.so.afu.gewaesserschutz'," +
                    "'published': '2021-12-23T14:54:49.050062'," +
                "}";

        String jsonMissingBasketAndRegion = rawJson.replaceAll("\'", "\"");

        assertMatchingException(jsonMissingBasketAndRegion, UpdatePublishTimeService.ERR_MSGBODY_INVALID);
    }

    private static void assertMatchingException(String jsonMessage, String exceptionMessage){

        CodedException ce = Assertions.assertThrows(CodedException.class, () -> PubNotification.parseFromJson(jsonMessage));
        Assertions.assertEquals(exceptionMessage, ce.getMessage());

        log.warn("Thrown as expected: {}", ce.toString());
    }
}