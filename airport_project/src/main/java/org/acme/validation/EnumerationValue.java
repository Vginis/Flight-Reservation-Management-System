package org.acme.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.acme.constant.ValueEnum;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {EnumerationValueValidator.class}
)
@Documented
public @interface EnumerationValue {
    String message() default "{enumeration.value.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends ValueEnum> acceptedEnum();

    String[] excludedValues() default {};
}
