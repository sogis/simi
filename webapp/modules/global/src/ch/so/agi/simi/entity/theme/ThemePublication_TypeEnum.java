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
        String res = null;

        if("tableRelational".equals(id))
            res = "relational";

        return res;
    }

    private String getDefaultSuffixNullAsEmpty(){
        if (getDefaultSuffix() != null)
            return getDefaultSuffix();
        else
            return "";
    }

    /**
     * Adapts the suffix to the type change.
     * @param prevType Previous themepub type
     * @param prevSuffix Previous suffix
     * @param currType Current themepub type
     * @return The potentially modified suffix
     */
    public static String adaptSuffixToTypeChange(ThemePublication_TypeEnum prevType, String prevSuffix, ThemePublication_TypeEnum currType){

        if(prevType == null) {
            if(prevSuffix != null)
                return prevSuffix;
            else
                return currType.getDefaultSuffix();
        }

        String res = null;

        String prevSuffixNullAsEmpty = prevSuffix;
        if(prevSuffixNullAsEmpty == null)
            prevSuffixNullAsEmpty = "";

        String prevDefaultNullAsEmpty = prevType.getDefaultSuffixNullAsEmpty();

        if(prevSuffixNullAsEmpty.equals(prevDefaultNullAsEmpty)){
            res = currType.getDefaultSuffix();
        }
        else{
            res = prevSuffix;
        }

        return res;
    }
}