package org.spring.web.handler.parameter.post;


import org.spring.annotations.autoconfig.Component;
import org.spring.utils.global.StringUtils;
import org.spring.web.handler.parameter.post.PostMethodParameterHandler;
import org.spring.web.server.servlet.SpringWebServletRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.AbstractMap;
import java.util.Map;

/**
 * 处理 POST json 格式数据
 */
@Component
public class PostJsonHandler implements PostMethodParameterHandler {
    @Override
    public Map.Entry<Boolean, Map<String, Object>> handler(HttpServletRequest request, Method method, Parameter parameter) {
        String bodyString = ((SpringWebServletRequestWrapper) request).getBodyString(); //获取bodyString
        Map<String, Object> jsonArrayMap = StringUtils.getJsonArrayMap(bodyString);
        return new AbstractMap.SimpleEntry<>(true,jsonArrayMap);
    }

    @Override
    public boolean allow(HttpServletRequest request) {
        if (request.getContentType().startsWith("application/json")){
            return true;
        }
        return false;
    }
}
