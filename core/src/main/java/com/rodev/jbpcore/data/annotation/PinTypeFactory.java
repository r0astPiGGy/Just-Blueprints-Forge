package com.rodev.jbpcore.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PinTypeFactory {

    String typeId();

}
