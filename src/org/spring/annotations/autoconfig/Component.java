package org.spring.annotations.autoconfig;


import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * 在类上使用，表示创建该类对象，并添加到容器中
 */
public @interface Component {
    String value() default ""; //指定存储的key，后续可以根据名称来获取该对象
}
