package org.spring.aop.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * 在方法执行前，调用指定方法
 */
public @interface Before {
    /**
     * 格式：
     *   全类名 方法名称
     *   如：com.xxx.xxx.User login 会为User反射出对象，并调用User中的login方法，如果不指定全类名，则在当前类中的方法查找该方法
     */
    String value(); //方法名称

    /**
     * 声明方法需要的参数类型（before方法允许获取目标方法的中的参数列表以及目标对象）
     * 注意：必须从左往右依次对应
     * class为参数的class对象与目标对象class
     */
    Class<?>[] methodParameterType() default Void.class;

    /**
     * 是否需要拦截目标方法
     * 要求当前执行 before 方法返回一个 boolean 类型
     * before 方法返回true，表示放行执行目标方法
     * 如果返回false，则表示拦截目标方法（不执行目标方法）
     */
    boolean intercept() default false; //默认关闭

    /**
     * 被拦截后执行的方法,方法参数必须与 before 方法参数一致
     */
    String invokeMethod() default "";
}
