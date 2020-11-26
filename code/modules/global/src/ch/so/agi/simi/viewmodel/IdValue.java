package ch.so.agi.simi.viewmodel;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import javax.validation.constraints.NotNull;

@MetaClass(name = "simi_IdValue")
public class IdValue extends BaseUuidEntity {
    private static final long serialVersionUID = 8753466961945663556L;

    @NotNull
    @MetaProperty(mandatory = true)
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}