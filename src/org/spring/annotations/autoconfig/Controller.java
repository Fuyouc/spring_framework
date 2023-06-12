package org.spring.annotations.autoconfig;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Controller {
    /**
     * controller的名称
     * @return
     */
    String name() default "";

    /**
     * Controller底下的访问前缀
     * @return
     */
    String prefix() default "";
}
