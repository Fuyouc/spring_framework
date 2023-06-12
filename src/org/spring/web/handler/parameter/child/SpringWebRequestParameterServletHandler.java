package org.spring.web.handler.parameter.child;

import org.spring.annotations.autoconfig.Component;
import org.spring.web.handler.parameter.SpringWebRequestParameterHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.AbstractMap;
import java.util.Map;

@Component
public class SpringWebRequestParameterServletHandler implements SpringWebRequestParameterHandler {
    @Override
    public Map.Entry<Boolean, Object> handler(HttpServletRequest request, HttpServletResponse response, Method method, Parameter parameter, Map<String, Object> cache) throws Exception {
        Class<?> type = parameter.getType();
        Object result = null;
        if (HttpServletRequest.class.isAssignableFrom(type)){
            result = request;
        }else if (HttpServletResponse.class.isAssignableFrom(type)){
            result = response;
        }else if (HttpSession.class.isAssignableFrom(type)){
            result = request.getSession();
        }
        return new AbstractMap.SimpleEntry<>(true,result);
    }

    @Override
    public boolean allow(Parameter parameter) {
        Class<?> type = parameter.getType();
        return HttpServletRequest.
                class.isAssignableFrom(type)
                || HttpServletResponse.class.isAssignableFrom(type)
                || HttpSession.class.isAssignableFrom(type);
    }
}
