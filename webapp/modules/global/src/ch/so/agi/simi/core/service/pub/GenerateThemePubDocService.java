package ch.so.agi.simi.core.service.pub;

import ch.so.agi.simi.global.exc.CodedException;

public interface GenerateThemePubDocService {
    String NAME = "simi_GenerateThemePubDocService";

    /**
     * Queries the data and generates the html doc sheet for the themepublication.
     * @param themePubIdent Identifier of the themepublication
     * @return The doc sheet html
     * @throws CodedException
     */
    String generateDoc(String themePubIdent) throws CodedException;
}