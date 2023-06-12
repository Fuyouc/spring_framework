package org.spring.annotations;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PostConstruct {
    /**
     * 在类实例化后，会调用该类中被 @PostConstruct 标志的Method
     * Method中如果有参数列表，会优先根据参数名进行查找，否则根据参数类型进行查找
     */
}
