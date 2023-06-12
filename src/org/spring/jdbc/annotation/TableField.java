package org.spring.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface TableField {
    /**
     * 标志在实体类字段上，value值为数据库的字段名称
     * 如果value为空，则使用字段名称代替
     * 将字段与数据库字段绑定
     */
    String value() default "";

    /**
     * 在进行其他操作时，是否将该字段排除在外
     */
    boolean exclude() default false;
}
