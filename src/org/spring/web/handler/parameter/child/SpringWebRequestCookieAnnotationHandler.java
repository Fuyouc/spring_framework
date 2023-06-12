package org.spring.web.handler.parameter.child;

import org.spring.annotations.autoconfig.Component;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;
import org.spring.web.annotations.request.parameter.RequestCookie;
import org.spring.web.annotations.request.parameter.RequestHeader;
import org.spring.web.exception.RequestParameterException;
import org.spring.web.handler.parameter.SpringWebRequestParameterHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Component
public class SpringWebRequestCookieAnnotationHandler implements SpringWebRequestParameterHandler {

    @Override
    public Map.Entry<Boolean, Object> handler(HttpServletRequest request, HttpServletResponse response, Method method, Parameter parameter, Map<String, Object> cache) throws Exception {
        Class<?> type = parameter.getType();
        Object result = null;
        if (Cookie.class.isAssignableFrom(type)){
            RequestCookie requestCookie = parameter.getAnnotation(RequestCookie.class);
            String key = StringUtils.isEmpty(requestCookie.value()) ? parameter.getName() : requestCookie.value();
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(key)){
                    result = cookie;
                    break;
                }
            }
            if (!requestCookie.required() && ObjectUtils.isEmpty(result)){
                throw new RequestParameterException("parameter " + key + " is null");
            }
        }else if (List.class.isAssignableFrom(type)){
            List<Cookie> list = new ArrayList<>();
            for (Cookie cookie : request.getCookies()) {
                list.add(cookie);
            }
            result = list;
        }
        return new AbstractMap.SimpleEntry<>(true,result);
    }

    @Override
    public boolean allow(Parameter parameter) {
        return !ObjectUtils.isEmpty(parameter.getAnnotation(RequestCookie.class));
    }
}
