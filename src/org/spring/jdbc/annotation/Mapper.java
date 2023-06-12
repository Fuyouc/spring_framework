package org.spring.jdbc.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mapper {
    /**
     * 标志在 Dao 层接口上，会为该接口通过动态代理生成一个类
     * @return
     */
    String value() default "";
}
