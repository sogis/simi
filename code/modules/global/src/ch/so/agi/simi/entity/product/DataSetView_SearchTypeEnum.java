package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum DataSetView_SearchTypeEnum implements EnumClass<Integer> {

    NEIN(10),
    IMMER(20),
    FALLS_GELADEN(30);

    private Integer id;

    DataSetView_SearchTypeEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static DataSetView_SearchTypeEnum fromId(Integer id) {
        for (DataSetView_SearchTypeEnum at : DataSetView_SearchTypeEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}