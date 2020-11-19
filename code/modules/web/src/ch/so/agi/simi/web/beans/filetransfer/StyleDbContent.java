package ch.so.agi.simi.web.beans.filetransfer;

import java.util.Optional;

/**
 * DTO for the style in the form of the database fields
 */
public class StyleDbContent {

    private String qmlContent;
    private String assetsContent;

    public StyleDbContent(String qmlContent, String assetsContent){

        if(qmlContent == null || qmlContent.length() == 0)
            throw new RuntimeException("qmlContent argument must not be null");

        this.qmlContent = qmlContent;
        this.assetsContent = assetsContent;
    }

    public String getQmlContent() {
        return qmlContent;
    }

    public Optional<String> getAssetsContent() {
        return Optional.ofNullable(assetsContent);
    }
}
