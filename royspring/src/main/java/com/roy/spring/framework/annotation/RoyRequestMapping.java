package com.roy.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @author Roy
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoyRequestMapping {

    String value() default "";
}
