package ch.so.agi.simi.entity.theme;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ThemePublicationType implements EnumClass<String> {

    ;

    private String id;

    ThemePublicationType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ThemePublicationType fromId(String id) {
        for (ThemePublicationType at : ThemePublicationType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}