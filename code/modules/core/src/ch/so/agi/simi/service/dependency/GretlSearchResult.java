package ch.so.agi.simi.service.dependency;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GretlSearchResult {
    private List<GretlSearchItem> items;

    public List<GretlSearchItem> getItems() {
        return items;
    }

    public void setItems(List<GretlSearchItem> items) {
        this.items = items;
    }
}
