package ch.so.agi.simi.viewmodel;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

@MetaClass(name = "viewModel_IdValue")
public class IdValue_ extends BaseUuidEntity {
    private static final long serialVersionUID = -3240248061040554134L;

    @MetaProperty
    private String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}