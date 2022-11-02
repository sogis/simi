package ch.so.agi.simi.core.service.pub;

import ch.so.agi.simi.global.exc.CodedException;

public interface GenerateThemePubDocService {
    String NAME = "simi_GenerateThemePubDocService";

    public static final String ERR_THEMEPUB_UNKNOWN = "ThemePublication reference is not known"; //404

    public static final String ERR_SERVER = "Processing of the reqest failed";  //500

    /**
     * Queries the data and generates the html doc sheet for the themepublication.
     * @param themePubIdent Identifier of the themepublication
     * @return The doc sheet html
     * @throws CodedException
     */
    String generateDoc(String themePubIdent) throws CodedException;
}