package ch.so.agi.simi.entity.product;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum DataProduct_PubScope_OverallStateEnum implements EnumClass<Integer> {

    PUBLISHED(10),
    NOT_SELF_PUBLISHED(20),
    TO_BE_DELETED(30);

    private Integer id;

    DataProduct_PubScope_OverallStateEnum(Integer value) {
        this.id = value;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Nullable
    public static DataProduct_PubScope_OverallStateEnum fromId(Integer id) {
        for (DataProduct_PubScope_OverallStateEnum at : DataProduct_PubScope_OverallStateEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}