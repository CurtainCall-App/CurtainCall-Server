package org.cmc.curtaincall.domain.core;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PageableAllowedSortValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedSort {

    String message() default "지원하지 않는 sort 입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    SortParam[] value() default {};
}
