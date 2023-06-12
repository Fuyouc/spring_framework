package org.spring.jdbc.sql.execute.handler.sql_handler;

import org.spring.annotations.autoconfig.Component;
import org.spring.jdbc.sql.execute.handler.SQLParameterValueHandler;
import org.spring.utils.global.ClassUtils;

import java.lang.reflect.Parameter;
import java.util.Map;

@Component
public class MapParameterHandler implements SQLParameterValueHandler {
    @Override
    public Map<String, Object> handler(Parameter parameter, Object value) {
        return (Map<String, Object>) value;
    }

    @Override
    public boolean release(Parameter parameter) {
        Class<?> type = parameter.getType();
        return Map.class.isAssignableFrom(type);
    }
}
