package com.roy.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @author Roy
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoyRequestParam {
    String value() default "";
}
