package com.rodev.jmcparser.patcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Patchable {

    String DEFAULT_INPUT_VALUE = "*";

    String correspondsTo() default DEFAULT_INPUT_VALUE;

}
