package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ExternalWmsLayer_FiFormat implements EnumClass<String> {

    GML("application/vnd.ogc.gml"),
    TEXT("text/plain"),
    FI_UNAVAILABLE("fi_unavailable");

    private String id;

    ExternalWmsLayer_FiFormat(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ExternalWmsLayer_FiFormat fromId(String id) {
        for (ExternalWmsLayer_FiFormat at : ExternalWmsLayer_FiFormat.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}