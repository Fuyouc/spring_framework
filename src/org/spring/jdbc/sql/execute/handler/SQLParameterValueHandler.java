package org.spring.jdbc.sql.execute.handler;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

public interface SQLParameterValueHandler {
    /**
     * 处理dao方法各个类型的参数与参数值，封装成一个Map返回
     */
    Map<String,Object> handler(String key,Parameter parameter,Object value);

    /**
     * 判断是否处理该类型的参数
     */
    boolean release(Parameter parameter);
}
