package org.spring.annotations.autoconfig;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Value {
    /**
     * 标记在字段上，从配置文件中获取值，如果类上方存在 @ConfigurationProperties 注解
     * 则使用 @ConfigurationProperties.value 属性作为前缀
     */

    String value();

    /**
     * rguo
     * @return
     */
    boolean prefix() default true;
}
