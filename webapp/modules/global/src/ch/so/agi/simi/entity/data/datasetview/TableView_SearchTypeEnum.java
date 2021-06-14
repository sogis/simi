package ch.so.agi.simi.entity.data.datasetview;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TableView_SearchTypeEnum implements EnumClass<String> {

    NO_SEARCH("1_no_search"),
    IF_LOADED("2_if_loaded"),
    ALWAYS("3_always");

    private String id;

    TableView_SearchTypeEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TableView_SearchTypeEnum fromId(String id) {
        for (TableView_SearchTypeEnum at : TableView_SearchTypeEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}