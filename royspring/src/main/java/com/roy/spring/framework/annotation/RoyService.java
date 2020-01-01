package com.roy.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @author Roy
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoyService {
    String value() default "";
}
