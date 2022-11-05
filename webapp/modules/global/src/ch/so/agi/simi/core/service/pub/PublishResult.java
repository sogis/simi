package ch.so.agi.simi.core.service.pub;

import java.io.Serializable;

public class PublishResult implements Serializable {
    private String themePubIdentifier;
    private int dbInsertCount;
    private int dbUpdateCount;

    public PublishResult(String themePubIdentifier, int dbInsertCount, int dbUpdateCount) {
        this.themePubIdentifier = themePubIdentifier;
        this.dbInsertCount = dbInsertCount;
        this.dbUpdateCount = dbUpdateCount;
    }

    public String getThemePubIdentifier() {
        return themePubIdentifier;
    }

    public int getDbInsertCount() {
        return dbInsertCount;
    }

    public int getDbUpdateCount() {
        return dbUpdateCount;
    }
}
