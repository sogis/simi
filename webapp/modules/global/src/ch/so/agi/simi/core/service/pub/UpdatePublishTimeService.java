package ch.so.agi.simi.core.service.pub;

import ch.so.agi.simi.global.exc.CodedException;
import org.apache.commons.lang3.tuple.Pair;

public interface UpdatePublishTimeService {
    String NAME = "simi_UpdatePublishTimeService";


    /**
     * Updates the corresponding collection of PublishedSubArea entries for a ThemePublication.
     *
     * If the jsonMessage references no SubArea, the update will reference the default region
     * "Kanton Solothurn".
     * The method signature is intentional "stupid" to cover json-parsing, error handling, ...
     * with cuba framework integration tests.
     * @param jsonMessage The payload of the PUT-Request (Json).
     * @return Result of the Publication.
     * @throws CodedException Contains http status code and message to be returned to the rest service client.
     */
    PublishResult update(String jsonMessage) throws CodedException;
}