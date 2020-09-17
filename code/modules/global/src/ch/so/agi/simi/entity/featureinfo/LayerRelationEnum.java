package ch.so.agi.simi.entity.featureinfo;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum LayerRelationEnum implements EnumClass<Integer> {

    IS_FOR_LAYER(10),
    QUERIES(20);

    private Integer id;

    LayerRelationEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static LayerRelationEnum fromId(Integer id) {
        for (LayerRelationEnum at : LayerRelationEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}