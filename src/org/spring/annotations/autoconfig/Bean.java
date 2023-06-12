package org.spring.annotations.autoconfig;



import org.spring.annotations.autoconfig.Configuration;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * 在 Configuration 注解类中使用
 * 标注在方法上，返回追会被注入容器
 * 名称默认使用方法名，如果value不为空，则使用它
 */
@Configuration
public @interface Bean{
    String value() default "";
}
