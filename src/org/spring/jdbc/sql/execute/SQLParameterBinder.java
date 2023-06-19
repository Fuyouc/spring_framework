package org.spring.jdbc.sql.execute;

import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.jdbc.annotation.Param;
import org.spring.jdbc.config.ParameterBindValue;
import org.spring.jdbc.sql.DaoMethod;
import org.spring.jdbc.sql.execute.handler.SQLParameterValueHandler;
import org.spring.jdbc.utils.SQLStringUtils;
import org.spring.utils.global.ObjectUtils;

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

    public List<ParameterBindValue> getParameterBindValue(DaoMethod daoMethod, List<String> sqlParameter){
        Method method = daoMethod.getMethod();
        Parameter[] parameters = method.getParameters();
        Map<String,Object> bindMap = new HashMap<>();
        String key = null;
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            key = ObjectUtils.isEmpty(param.getAnnotation(Param.class)) ? param.getName() : param.getAnnotation(Param.class).value();
            for (SQLParameterValueHandler handler : sqlParameterValueHandlers) {
                if (handler.release(param)){
                    Map<String, Object> result = handler.handler(key,param,daoMethod.getValue(i));
                    Iterator<Map.Entry<String, Object>> iterator = result.entrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry<String, Object> entry = iterator.next();
                        bindMap.put(entry.getKey(),entry.getValue());
                    }
                    break;
                }
            }
        }

        Map<String, Queue<Object>> mapperParam = daoMethod.getMapperParam();
        List<ParameterBindValue> parameterBindValues = new ArrayList<>();
        for (int i = 0; i < sqlParameter.size(); i++) {
            String param = sqlParameter.get(i);
            Object value = bindMap.get(param);
            if (ObjectUtils.isEmpty(value)) {
                //如果Value为空，尝试去与xml文件配置的映射参数中查找
                if (!ObjectUtils.isEmpty(mapperParam)){
                    Queue<Object> queue = mapperParam.get(param);
                    if (!ObjectUtils.isEmpty(queue) && !queue.isEmpty()){
                        value = queue.poll();
                    }
                }
            }
            parameterBindValues.add(new ParameterBindValue(i, param, value));
        }
        return parameterBindValues;
    }
}
