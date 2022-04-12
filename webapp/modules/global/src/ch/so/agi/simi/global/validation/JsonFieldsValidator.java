package ch.so.agi.simi.global.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JsonFieldsValidator implements ConstraintValidator<JsonFields, StringFieldClass> {

    private String[] fieldNames;

    @Override
    public void initialize(JsonFields constraintAnnotation) {
        this.fieldNames = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(StringFieldClass cls, ConstraintValidatorContext context) {

        boolean validFields = true;

        for(String fieldName : fieldNames){
            String val = cls.getValue(fieldName);

            if(val != null) {
                if(!isValid(val))
                {
                    validFields = false;
                    break;
                }
            }
        }

        return validFields;
    }

    private static boolean isValid(String value){
        return "json".equals(value);
    }
}
