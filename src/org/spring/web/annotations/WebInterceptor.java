package org.spring.web.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebInterceptor {
    /**
     * 在实现 SpringWebInterceptor 接口的类上标志，表示当前是 SpringWebInterceptor 拦截器
     * 在访问指定的URI时，会使用value指去比较返回的URI前缀，如果相等，则交给该拦截器进行处理
     */
    String value();
}
