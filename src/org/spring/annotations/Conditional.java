package org.spring.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Conditional {
    /**
     * 如果容器中有指定的 bean，才创建该对象
     */
    Class<?>[] value() default {};

    /**
     * 如果容器中有指定的beanName，才去创建对象
     * @return
     */
    String[] name() default {};
}
