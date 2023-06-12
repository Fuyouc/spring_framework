package org.spring.web.handler.parameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public interface SpringWebRequestParameterHandler {
    Map.Entry<Boolean,Object> handler(HttpServletRequest request, HttpServletResponse response, Method method, Parameter parameter, Map<String,Object> cache) throws Exception;
    boolean allow(Parameter parameter);
}
