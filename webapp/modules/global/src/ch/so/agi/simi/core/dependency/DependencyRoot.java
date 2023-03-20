package ch.so.agi.simi.core.dependency;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class DependencyRoot implements Serializable {
    private List<UUID> ids;
    private String display;

    public DependencyRoot(List<UUID> ids, String display) {
        this.ids = ids;
        this.display = display;
    }

    public List<UUID> getIds() {
        return ids;
    }

    public String getDisplay() {
        return display;
    }
}

