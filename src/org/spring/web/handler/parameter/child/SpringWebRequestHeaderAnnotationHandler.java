package org.spring.web.handler.parameter.child;

import org.spring.annotations.autoconfig.Component;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;
import org.spring.web.annotations.request.parameter.RequestHeader;
import org.spring.web.exception.RequestParameterException;
import org.spring.web.handler.parameter.SpringWebRequestParameterHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class SpringWebRequestHeaderAnnotationHandler implements SpringWebRequestParameterHandler {

    @Override
    public Map.Entry<Boolean, Object> handler(HttpServletRequest request, HttpServletResponse response, Method method, Parameter parameter, Map<String, Object> cache) throws Exception {
        Class<?> type = parameter.getType();
        Object result = null;
        if (String.class.isAssignableFrom(type)){
            RequestHeader header = parameter.getAnnotation(RequestHeader.class);
            String key = StringUtils.isEmpty(header.value()) ? parameter.getName() : header.value();
            result = request.getHeader(key);
            if (!header.required() && ObjectUtils.isEmpty(result)){
                throw new RequestParameterException("parameter " + key + " is null");
            }
        }else if (Map.class.isAssignableFrom(type)){
            Map<String,String> map = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()){
                String key = headerNames.nextElement();
                map.put(key,request.getHeader(key));
            }
            result = map;
        }
        return new AbstractMap.SimpleEntry<>(true,result);
    }

    @Override
    public boolean allow(Parameter parameter) {
        return !ObjectUtils.isEmpty(parameter.getAnnotation(RequestHeader.class));
    }
}
