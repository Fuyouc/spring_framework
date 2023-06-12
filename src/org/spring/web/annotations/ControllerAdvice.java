package org.spring.web.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ControllerAdvice {
    /**
     * 在类上方标记，表示该类是一个异常处理器，会自动注入到容器中
     * 注意：只能捕获 Controller 层抛出的异常
     * 在捕获到异常后，会被该标记的类进行处理，会扫描类中的方法上是否存在 @ExceptionHandler 注解
     * 根据处理的类型交给对应的方法进行处理
     * 用法可参考 SpringBoot 异常处理机制
     */
    String value() default ""; //表示注入容器的名称
}
