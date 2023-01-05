package ch.so.agi.simi.core.service.pub.request;

import ch.so.agi.simi.global.exc.CodedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PubNotificationTest {

    private static Logger log = LoggerFactory.getLogger(PubNotificationTest.class);

    @Test
    void parse_Null_Throws() {
        assertMatchingException(null, CodedException.ERR_MSGBODY_EMPTY);
    }

    @Test
    void parse_EmptyString_Throws() {
        assertMatchingException("", CodedException.ERR_MSGBODY_EMPTY);
    }

    @Test
    void parse_NonParseableJson_Throws() {
        assertMatchingException("{]", CodedException.ERR_MSGBODY_INVALID);
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

        assertMatchingException(jsonMissingTimeStamp, CodedException.ERR_MSGBODY_INVALID);
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

        assertMatchingException(jsonWrongFieldType, CodedException.ERR_MSGBODY_INVALID);
    }

    @Test
    void parse_SunnyBasketJson_OK() {
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
    void serialize_OK() {
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

        PubNotification testObj = PubNotification.parseFromJson(sunnyJson);
        String json = testObj.toJson();

        PubNotification.parseFromJson(json);
    }

    @Test
    void parse_SunnyRegionJson_OK() {
        String rawJson = "{" +
                    "'dataIdent': 'ch.so.afu.gewaesserschutz'," +
                    "'published': '2021-12-23T14:54:49.050062'," +
                    "'publishedRegions': [{" +
                        "'region': 'olten'," +
                            "'publishedBaskets': [{" +
                            "'model': 'SO_AGI_MOpublic_20201009'," +
                            "'topic': 'Bodenbedeckung'," +
                            "'basket': 'oltenBID'" +
                        "}]" +
                        "}, {" +
                        "'region': 'wangen'," +
                            "'publishedBaskets': [{" +
                            "'model': 'SO_AGI_MOpublic_20201009'," +
                            "'topic': 'Bodenbedeckung'," +
                            "'basket': 'wangenBID'" +
                        "}]" +
                    "}]" +
                "}";
        String sunnyJson = rawJson.replaceAll("\'", "\"");

        PubNotification.parseFromJson(sunnyJson);
    }

    @Test
    void parse_JsonMissingBasketAndRegion_OK() {
        String rawJson = "{" +
                    "'dataIdent': 'ch.so.afu.gewaesserschutz'," +
                    "'published': '2021-12-23T14:54:49.050062'" +
                "}";

        String jsonMissingBasketAndRegion = rawJson.replaceAll("\'", "\"");

        PubNotification.parseFromJson(jsonMissingBasketAndRegion);
    }

    @Test
    void deriveModelFromRegions_OK() {
        String rawJson = "{" +
                    "'dataIdent': 'ch.so.afu.gewaesserschutz'," +
                    "'published': '2021-12-23T14:54:49.050062'," +
                    "'publishedRegions': [{" +
                        "'region': 'olten'," +
                            "'publishedBaskets': [{" +
                            "'model': 'SO_AGI_MOpublic_20201009'," +
                            "'topic': 'Bodenbedeckung'," +
                            "'basket': 'oltenBID'" +
                        "}]" +
                        "}, {" +
                        "'region': 'wangen'," +
                            "'publishedBaskets': [{" +
                            "'model': 'SO_AGI_MOpublic_20201009'," +
                            "'topic': 'Bodenbedeckung'," +
                            "'basket': 'wangenBID'" +
                        "}]" +
                    "}]" +
                "}";
        String sunnyJson = rawJson.replaceAll("\'", "\"");
        PubNotification p = PubNotification.parseFromJson(sunnyJson);

        Assertions.assertEquals("SO_AGI_MOpublic_20201009", p.deferModelName().orElse("NULL"));
    }

    @Test
    void deriveModelFromBaskets_OK() {
        String rawJson = "{" +
            "'dataIdent': 'ch.so.afu.gewaesserschutz'," +
            "'published': '2021-12-23T14:54:49.050062'," +
            "'publishedBaskets': [{" +
                "'model': 'SO_AGI_MOpublic_20201009'," +
                "'topic': 'Bodenbedeckung'," +
                "'basket': 'oltenBID'" +
            "},{" +
                "'model': 'SO_AGI_MOpublic_20201009'," +
                "'topic': 'Bodenbedeckung'," +
                "'basket': 'wangenBID'" +
            "}]" +
        "}";

        String sunnyJson = rawJson.replaceAll("\'", "\"");
        PubNotification p = PubNotification.parseFromJson(sunnyJson);

        Assertions.assertEquals("SO_AGI_MOpublic_20201009", p.deferModelName().orElse("NULL"));
    }

    @Test
    void deriveModel_NoRegionNoBasket_OK() {
        String rawJson = "{" +
                    "'dataIdent': 'ch.so.afu.gewaesserschutz'," +
                    "'published': '2021-12-23T14:54:49.050062'" +
                "}";

        String jsonMissingBasketAndRegion = rawJson.replaceAll("\'", "\"");

        PubNotification p = PubNotification.parseFromJson(jsonMissingBasketAndRegion);

        Assertions.assertFalse(p.deferModelName().isPresent());
    }

    private static void assertMatchingException(String jsonMessage, String exceptionMessage){

        CodedException ce = Assertions.assertThrows(CodedException.class, () -> PubNotification.parseFromJson(jsonMessage));
        Assertions.assertEquals(exceptionMessage, ce.getMessage());

        log.warn("Thrown as expected: {}", ce.toString());
    }
}