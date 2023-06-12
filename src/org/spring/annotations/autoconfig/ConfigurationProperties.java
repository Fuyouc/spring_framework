package org.spring.annotations.autoconfig;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * 与配置文件绑定，value为配置文件的前缀 xxx.xxx.xxx.
 * 会让该类中的字段名与配置文件后缀绑定
 */
public @interface ConfigurationProperties {
    String prefix() default "";
}
