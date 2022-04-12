package ch.so.agi.simi.global.validation;

import ch.so.agi.simi.global.SimiException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class StringFieldMock implements StringFieldClass {

    private String field;

    @Override
    public @NotNull String getValue(@NotEmpty String fieldName) {
        if(!"field".equals(fieldName))
            throw new SimiException("Field not found");

        return field;
    }

    public void setFieldValue(String value){
        this.field = value;
    }
}
