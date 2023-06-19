package org.spring.jdbc.sql.execute.handler;

import org.spring.annotations.autoconfig.Component;
import org.spring.jdbc.annotation.Param;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理参数是基本类型的（如，int、String、double等）
 */
@Component
public class BasicParameterHandler implements SQLParameterValueHandler {
    @Override
    public Map<String, Object> handler(String key,Parameter parameter, Object value) {
        Map<String,Object> map = new HashMap<>();
        if (!ObjectUtils.isEmpty(parameter.getAnnotation(Param.class))){
            key = parameter.getAnnotation(Param.class).value();
        }
        map.put(key,value);
        return map;
    }

    @Override
    public boolean release(Parameter parameter) {
        Class<?> type = parameter.getType();
        return ClassUtils.isWrapClass(type);
    }
}
