package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum DataSetView_SearchTypeEnum_Int implements EnumClass<Integer> {

    NEIN(10),
    IMMER(20),
    FALLS_GELADEN(30);

    private Integer id;

    DataSetView_SearchTypeEnum_Int(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static DataSetView_SearchTypeEnum_Int fromId(Integer id) {
        for (DataSetView_SearchTypeEnum_Int at : DataSetView_SearchTypeEnum_Int.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}