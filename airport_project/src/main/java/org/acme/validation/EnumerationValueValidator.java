package org.acme.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.acme.constant.ValueEnum;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumerationValueValidator implements ConstraintValidator<EnumerationValue, Object> {
    private Set<String> acceptedValues;
    private Set<String> excludedValues;

    @Override
    public void initialize(EnumerationValue constraintAnnotation) {
        Class<? extends ValueEnum> enumClass = constraintAnnotation.acceptedEnum();
        this.excludedValues = new HashSet<>(Arrays.asList(constraintAnnotation.excludedValues()));

        this.acceptedValues = Arrays.stream(enumClass.getEnumConstants())
                .map(ValueEnum::value)
                .filter(value -> !excludedValues.contains(value))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context){
        if(value == null){
            return true;
        }
        boolean isValid = isEntityAcceptable(value);

        if (!isValid) {
            HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
            hibernateContext.disableDefaultConstraintViolation();
            hibernateContext.addMessageParameter("acceptedValues", acceptedValues)
                    .buildConstraintViolationWithTemplate(hibernateContext.getDefaultConstraintMessageTemplate())
                    .addConstraintViolation();
        }
        return isValid;
    }

    private boolean isEntityAcceptable(Object value){
        return (value instanceof List<?> valueList) ? isValueListAcceptable(valueList) : isValueAcceptable(value);
    }

    private boolean isValueAcceptable(Object value){
        String stringValue = String.valueOf(value);
        return acceptedValues.stream().anyMatch(v -> v.equalsIgnoreCase(stringValue))
                && excludedValues.stream().noneMatch(v -> v.equalsIgnoreCase(stringValue));
    }

    private boolean isValueListAcceptable(List<?> valueList){
        for (Object valueElement : valueList){
            String stringValue = String.valueOf(valueElement);
            if(!isValueAcceptable(stringValue)){
                return false;
            }
        }

        return true;
    }
}
