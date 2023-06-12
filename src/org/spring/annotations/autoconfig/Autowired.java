package org.spring.annotations.autoconfig;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * 将容器中的对象映射到字段的对象上
 *  - Object
 *      如果目标为普通Object，value为空，则根据类名来注入，如果类名无法注入，则根据类型注入
 *  - List
 *      如果目标为List<?>，首先根据value查找对象，如果找不到，则根据泛型类型来注入
 *  - Map
 *      如果目标为Map，首先根据value查找对象，如果找不到，则根据泛型类型来注入
 */
public @interface Autowired {
    String value() default "";
}
