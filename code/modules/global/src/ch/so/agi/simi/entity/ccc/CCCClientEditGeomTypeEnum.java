package ch.so.agi.simi.entity.ccc;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum CCCClientEditGeomTypeEnum implements EnumClass<Integer> {

    POINT(10),
    LINE(20),
    POLYGON(30);

    private Integer id;

    CCCClientEditGeomTypeEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static CCCClientEditGeomTypeEnum fromId(Integer id) {
        for (CCCClientEditGeomTypeEnum at : CCCClientEditGeomTypeEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}