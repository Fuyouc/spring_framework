package org.spring.web.json.annotations;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonIgnore {
    /**
     * 在实体类的字段上标志，表示忽略该字段，不会对该字段转换成json并且不响应给客户端
     */
}
