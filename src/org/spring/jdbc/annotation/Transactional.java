package org.spring.jdbc.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface Transactional {
    /**
     * 事务注解（功能暂时未开发）
     * 在方法上标记，表示该方法开启事务处理，当方法在没有异常的情况下执行结束后，则提交事务，否则回滚
     * 在类上标记，表示该类所有的 public 方法都是开启事务的
     */
}
