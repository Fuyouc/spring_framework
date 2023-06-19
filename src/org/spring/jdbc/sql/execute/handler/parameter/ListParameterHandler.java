package org.spring.jdbc.sql.execute.handler.parameter;

import org.spring.annotations.autoconfig.Component;
import org.spring.jdbc.annotation.Param;
import org.spring.jdbc.sql.execute.handler.SQLParameterValueHandler;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ListParameterHandler implements SQLParameterValueHandler {
    @Override
    public Map<String, Object> handler(String key,Parameter parameter, Object value) {
        Map<String,Object> map = new HashMap<>();
        map.put(key,value);
        return map;
    }

    @Override
    public boolean release(Parameter parameter) {
        Class<?> type = parameter.getType();
        return List.class.isAssignableFrom(type);
    }
}
