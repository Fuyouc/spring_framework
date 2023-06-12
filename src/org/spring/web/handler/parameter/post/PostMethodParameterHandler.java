package org.spring.web.handler.parameter.post;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * POST请求参数处理器
 */
public interface PostMethodParameterHandler {
    Map.Entry<Boolean,Map<String,Object>> handler(HttpServletRequest request, Method method, Parameter parameter);
    boolean allow(HttpServletRequest request);
}
