package org.spring.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotJson {
    /**
     * 标志在类的字段上，表示在转换json时，忽略该字段
     */
}
