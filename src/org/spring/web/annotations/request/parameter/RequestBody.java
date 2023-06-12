package org.spring.web.annotations.request.parameter;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestBody {
    /**
     * 从请求体中获取json数据
     * 支持
     *  - 实体类
     *  - List<实体类>
     *  - Map<String,Object>
     *  - List<Map<String,Object>
     */
}
