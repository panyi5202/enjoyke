package com.roy.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @author Roy
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoyAutowired {
    boolean required() default true;

    String value() default "";
}
