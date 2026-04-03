package ch.so.agi.simi.global.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Klassen-Level-Constraint für CCCIntegration:
 * Entweder muss "map" gesetzt sein ODER das Feld "notifyLayers" muss einen Json-Schlüssel "layers" enthalten.
 *
 * Damit die Validierung vor dem Speichern in Cuba angestossen wird, muss die
 * korrekte Validierungsgruppe angegeben werden: @MapOrNotifyLayersRequired(groups = {UiComponentChecks.class})
 */
@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = MapOrNotifyLayersRequiredValidator.class)
public @interface MapOrNotifyLayersRequired {

    String message() default "Karte muss gesetzt sein ODER Rückaufruf-Konfig muss ein Feld 'layers' enthalten.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
