package ch.so.agi.simi.global.validation;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = JsonFieldsValidator.class)
public @interface JsonFields {

    String[] fields() default {};

    String message() default "Feldinhalt muss gültiges Json sein. Json-Feld(er) des Objektes: {fields}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
