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
 * korrekte Validierungsgruppe angegeben werden: @JsonField(groups = {UiComponentChecks.class})
 */
@Target({ ElementType.FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = JsonFieldValidator.class)
public @interface JsonField {

    String message() default "Feldinhalt muss gültiges Json sein.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
