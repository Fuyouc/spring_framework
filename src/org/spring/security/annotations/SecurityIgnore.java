package org.spring.security.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecurityIgnore {
    /**
     * 在 RequestMapping 的方法中标志，表示忽略该方法的加密与解密的过程
     */
}
