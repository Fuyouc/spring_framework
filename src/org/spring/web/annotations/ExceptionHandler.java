package org.spring.web.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionHandler {
    /**
     * 在 @ControllerAdvice 类中标记的方法上标记，表示这是一个处理指定异常的方法
     * 当捕获到异常后，会根据 exceptionClasss属性 的值来辨别处理的异常类型
     * exceptionClass：异常.class 对象，表示如果捕获到该异常，则调用该方法
     * 方法允许没有参数，如果需要参数，必须在方法上声明以下三个参数
     *  - 1、HttpServletRequest
     *  - 2、HttpServletResponse
     *  - 3、Exception（异常信息）
     */
    Class<?>[] exceptionClass() default Void.class;
}
