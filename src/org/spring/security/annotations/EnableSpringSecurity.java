package org.spring.security.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableSpringSecurity {
    //如果需要使用Security的功能，则需要在启动类中标志@EnableSpringSecurity，会开启加密校验
}
