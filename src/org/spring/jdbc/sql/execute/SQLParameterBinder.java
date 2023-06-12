package org.spring.jdbc.sql.execute;

import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.jdbc.config.ParameterBindValue;
import org.spring.jdbc.sql.execute.handler.SQLParameterValueHandler;
import org.spring.jdbc.utils.SQLStringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * SQL参数绑定器
 */
@Component
public final class SQLParameterBinder {

    @Autowired
    private List<SQLParameterValueHandler> sqlParameterValueHandlers;

    public List<ParameterBindValue> getParameterBindValue(Method method,Object[] paramValues,String SQL,List<String> sqlParameter){
        Parameter[] parameters = method.getParameters();
        Map<String,Object> bindMap = new HashMap<>();
        for (int i = 0; i < method.getParameters().length; i++) {
            Parameter param = parameters[i];
            for (SQLParameterValueHandler handler : sqlParameterValueHandlers) {
                if (handler.release(param)){
                    Map<String, Object> result = handler.handler(param, paramValues[i]);
                    Iterator<Map.Entry<String, Object>> iterator = result.entrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry<String, Object> entry = iterator.next();
                        bindMap.put(entry.getKey(),entry.getValue());
                    }
                    break;
                }
            }
        }
        List<ParameterBindValue> parameterBindValues = new ArrayList<>();
        for (int i = 0; i < sqlParameter.size(); i++) {
            String param = sqlParameter.get(i);
            Object value = bindMap.get(param);
            parameterBindValues.add(new ParameterBindValue(i,param,value));
        }
        return parameterBindValues;
    }
}
