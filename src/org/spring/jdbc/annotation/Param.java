package org.spring.jdbc.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {
    /**
     * Dao 层方法上的参数上标志
     * 会与 SQL 中的 #{参数名} 与 value 属性进行比较
     * 如果value为空，则采用参数名代替
     */
    String value();
}
