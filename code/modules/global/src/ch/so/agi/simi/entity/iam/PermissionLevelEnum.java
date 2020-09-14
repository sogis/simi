package ch.so.agi.simi.entity.iam;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum PermissionLevelEnum implements EnumClass<String> {

    READ("1_read"),
    READ_WRITE("2_read_write");

    private String id;

    PermissionLevelEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static PermissionLevelEnum fromId(String id) {
        for (PermissionLevelEnum at : PermissionLevelEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}