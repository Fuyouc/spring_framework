package org.spring.web.annotations.request.parameter;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestHeader {

    String value() default "";

    boolean required() default true;
}
