package org.spring.aop.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Advice {
    /**
     * 在类上方标志，该类必须注入到容器中，并且实现某个接口
     * 会为目标类根据动态代理生成代理类，获取目标类方法上的 @Before、@After 注解
     * 根据配置执行切片操作
     * 具体实现请看 AopClassHandler
     */
}
