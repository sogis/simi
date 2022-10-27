package ch.so.agi.simi.core.service.pub;

import ch.so.agi.simi.global.exc.CodedException;

public interface UpdatePublishTimeService {
    String NAME = "simi_UpdatePublishTimeService";

    public static final String ERR_MSGBODY_EMPTY =  "Message body is empty"; //400
    public static final String ERR_MSGBODY_INVALID =  "Message body json is invalid or incomplete"; //400

    public static final String ERR_THEMEPUB_UNKNOWN = "ThemePublication reference is not known"; //404
    public static final String ERR_SUBAREA_UNKNOWN = "SubArea (region) reference is not known";  //404

    public static final String ERR_SERVER = "Processing of the reqest failed";  //500


    /**
     * Updates the corresponding collection of PublishedSubArea entries for a ThemePublication.
     *
     * If the jsonMessage references no SubArea, the update will reference the default region
     * "Kanton Solothurn".
     * The method signature is intentional "stupid" to cover json-parsing, error handling, ...
     * with cuba framework integration tests.
     * @param jsonMessage The payload of the PUT-Request (Json).
     * @throws CodedException Contains http status code and message to be returned to the rest service client.
     */
    void update(String jsonMessage) throws CodedException;
}