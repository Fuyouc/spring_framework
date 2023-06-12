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

    /**
     * 是否将 VALUES(#{参数}) 中的参数拼接到 表名(参数) 中
     * 要求：必须保证参数名与数据库中的字段对的上
     */
    boolean autoSplicing() default false;
}
