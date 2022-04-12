package ch.so.agi.simi.global.validation;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface StringFieldClass {
    @NotNull
    String getValue(@NotEmpty String fieldName);
}
