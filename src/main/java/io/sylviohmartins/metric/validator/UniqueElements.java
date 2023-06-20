package io.sylviohmartins.metric.validator;

import io.sylviohmartins.metric.validator.internal.UniqueElementsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = UniqueElementsValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(UniqueElements.List.class)
public @interface UniqueElements {

    String message() default "Não pode haver elementos duplicados na lista";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({TYPE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {

        UniqueElements[] value();
    }

}
