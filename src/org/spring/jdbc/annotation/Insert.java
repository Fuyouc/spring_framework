package org.spring.jdbc.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@SQL
public @interface Insert {
    /**
     * SQL语句
     */
    String value() default "";
}
