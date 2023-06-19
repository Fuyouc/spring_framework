package org.spring.jdbc.sql.execute.handler.parameter;

import org.spring.annotations.autoconfig.Component;
import org.spring.jdbc.sql.execute.handler.SQLParameterValueHandler;
import org.spring.utils.global.ClassUtils;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class MapParameterHandler implements SQLParameterValueHandler {
    @Override
    public Map<String, Object> handler(String key,Parameter parameter, Object value) {
        Map<String,Object> oldMap = (Map<String, Object>) value;
        Map<String,Object> newMap = new HashMap<>();
        Iterator<Map.Entry<String, Object>> iterator = oldMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> entry = iterator.next();
            newMap.put(key + "." + entry.getKey(),entry.getValue());
        }
        return newMap;
    }

    @Override
    public boolean release(Parameter parameter) {
        Class<?> type = parameter.getType();
        return Map.class.isAssignableFrom(type);
    }
}
