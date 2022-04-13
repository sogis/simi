package ch.so.agi.simi.global.validation;

import ch.so.agi.simi.global.SimiException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class JsonFieldsMock {

    @JsonField
    public String nullString;
    @JsonField
    public String wellFormedJson;
    @JsonField
    public String nonJsonString;
}
