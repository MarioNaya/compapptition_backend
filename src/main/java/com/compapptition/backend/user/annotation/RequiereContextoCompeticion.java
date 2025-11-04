package com.compapptition.backend.user.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiereContextoCompeticion {
    boolean value() default true;
}