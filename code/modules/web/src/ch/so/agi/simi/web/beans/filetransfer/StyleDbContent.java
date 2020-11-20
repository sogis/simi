package ch.so.agi.simi.web.beans.filetransfer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * DTO for the style in the form of the database fields
 */
public class StyleDbContent {

    private String qmlContent;
    private HashMap<String, byte[]> assets;

    public StyleDbContent(String qmlContent, HashMap<String, byte[]> assets){

        if(qmlContent == null || qmlContent.length() == 0)
            throw new RuntimeException("qmlContent argument must not be null");

        this.qmlContent = qmlContent;
        this.assets = assets;
    }

    public String getQmlContent() {
        return qmlContent;
    }

    public Optional<HashMap<String, byte[]>> getAssets() {
        return Optional.ofNullable(assets);
    }
}
