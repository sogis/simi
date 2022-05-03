package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ExternalWmsService_FiFormat implements EnumClass<String> {

    GML("application/vnd.ogc.gml"),
    TEXT("text/plain"),
    FI_UNAVAILABLE("fi_unavailable");

    private String id;

    ExternalWmsService_FiFormat(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ExternalWmsService_FiFormat fromId(String id) {
        for (ExternalWmsService_FiFormat at : ExternalWmsService_FiFormat.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}