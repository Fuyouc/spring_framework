package org.spring.aop.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
/**
 * 在方法执行后，调用目标方法
 */
public @interface After {
    /**
     * 同 before 注解一致
     */
    String value(); //方法名称

    /**
     * 声明方法需要的参数类型（after方法允许获取目标方法的中的参数列表以及返回结果和目标对象）
     * 注意：必须从左往右依次对应
     * class为参数的class对象与方法返回值class对象与目标对象class
     */
    Class<?>[] methodParameterType() default Void.class; //声明方法需要的参数类型

    /**
     * 如果After方法需要返回值类型，则参数列表要与目标方法一样，
     * 然后再后面添加目标方法的返回值，并且也需要在 methodParameterType 中声明
     */
    boolean result() default false; //是否需要目标方法的返回值结果
}
