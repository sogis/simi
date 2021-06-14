package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ExternalMapService_Type implements EnumClass<String> {

    WMS("WMS"),
    WMTS("WMTS");

    private String id;

    ExternalMapService_Type(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ExternalMapService_Type fromId(String id) {
        for (ExternalMapService_Type at : ExternalMapService_Type.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}