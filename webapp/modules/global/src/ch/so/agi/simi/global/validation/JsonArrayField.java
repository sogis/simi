package ch.so.agi.simi.global.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validiert, ob der Inhalt eines String-Feldes als Json geparsed werden kann.
 *
 * Damit die Validierung vor dem Speichern in Cuba angestossen wird, muss die
 * korrekte Validierungsgruppe angegeben werden: @JsonArrayField(groups = {UiComponentChecks.class})
 */
@Target({ ElementType.FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = JsonArrayUtil.class)
public @interface JsonArrayField {

    String message() default "Feldinhalt muss ein flaches Json-Array sein.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
