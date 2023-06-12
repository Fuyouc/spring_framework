package org.spring.web.annotations.request.parameter;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {

    /**
     * 从 request 中的 Params 中获取参数值
     */

    //参数Key，如果为空，则使用字段名
    String value() default "";

    //是否允许该参数值为空，默认为true
    boolean required() default true;
}
