package org.spring.annotations.autoconfig;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * 表示当前类为配置类
 */
@Component
public @interface Configuration {
    boolean isEnable() default true; //是否注入容器
    String value() default "";       //名称
}
