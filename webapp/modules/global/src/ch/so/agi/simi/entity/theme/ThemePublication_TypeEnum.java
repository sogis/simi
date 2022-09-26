package ch.so.agi.simi.entity.theme;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ThemePublication_TypeEnum implements EnumClass<String> {

    TABLE_SIMPLE("tableSimple"),
    TABLE_RELATIONAL("tableRelational"),
    NON_TABULAR("nonTabular");

    private String id;

    ThemePublication_TypeEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ThemePublication_TypeEnum fromId(String id) {
        for (ThemePublication_TypeEnum at : ThemePublication_TypeEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

    public String getDefaultSuffix(){
        String res = "";

        if("tableRelational".equals(id))
            res = "relational";

        return res;
    }
}