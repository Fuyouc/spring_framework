package org.spring.jdbc.sql.execute.handler;

import org.spring.annotations.autoconfig.Component;
import org.spring.jdbc.annotation.TableField;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理参数是一个普通对象
 */
@Component
public class ObjectParameterHandler implements SQLParameterValueHandler {
    @Override
    public Map<String, Object> handler(Parameter parameter, Object value) {
        Map<String,Object> map = new HashMap<>();
        try {
            Class<?> targetClass = value.getClass();
            for (Field field : targetClass.getDeclaredFields()) {
                field.setAccessible(true);
                String name = field.getName();
                if (!ObjectUtils.isEmpty(field.getAnnotation(TableField.class))) name = field.getAnnotation(TableField.class).value();
                map.put(name,field.get(value));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return map;
        }
    }

    @Override
    public boolean release(Parameter parameter) {
        Class<?> type = parameter.getType();
        return !List.class.isAssignableFrom(type) &&
                !Map.class.isAssignableFrom(type) &&
                !ClassUtils.isWrapClass(type);
    }
}
