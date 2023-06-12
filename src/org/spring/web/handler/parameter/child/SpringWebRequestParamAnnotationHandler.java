package org.spring.web.handler.parameter.child;

import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;
import org.spring.web.annotations.request.parameter.RequestParam;
import org.spring.web.exception.RequestParameterException;
import org.spring.web.handler.parameter.SpringWebRequestParameterHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Component
public class SpringWebRequestParamAnnotationHandler implements SpringWebRequestParameterHandler {

    @Autowired
    private SpringWebRequestParameterServletHandler servletHandler;

    @Override
    public Map.Entry<Boolean,Object> handler(HttpServletRequest request, HttpServletResponse response, Method method, Parameter parameter, Map<String,Object> cache) throws Exception {
        RequestParam param = parameter.getAnnotation(RequestParam.class);
        String key = ObjectUtils.isEmpty(param) ? parameter.getName() : param.value();
        Class<?> type = parameter.getType();
        Map.Entry<String,Object> result = null;
        if (ClassUtils.isWrapClass(type)){
            result = basic(request,parameter);
        } else if (List.class.isAssignableFrom(type)){
            result = list(key,request);
        } else if (Map.class.isAssignableFrom(type)){
            result = map(request,parameter);
        }else if (!ClassUtils.isWrapClass(type)) {
            result = object(request, parameter);
        }
        if (!ObjectUtils.isEmpty(param) && !param.required() && ObjectUtils.isEmpty(request)) throw new RequestParameterException("parameter " + result.getKey() + " is null");
        return new AbstractMap.SimpleEntry<>(true,result.getValue());
    }

    @Override
    public boolean allow(Parameter parameter) {
        return !servletHandler.allow(parameter) && !ObjectUtils.isEmpty(parameter.getAnnotation(RequestParam.class)) || parameter.getAnnotations().length == 0;
    }

    private Map.Entry<String,Object> basic(HttpServletRequest request,Parameter parameter){
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        String key = ObjectUtils.isEmpty(requestParam) ? parameter.getName() : requestParam.value();
        return new AbstractMap.SimpleEntry<>(key,basic(parameter.getType(),request.getParameter(key)));
    }

    private Map.Entry<String,Object> object(HttpServletRequest request,Parameter parameter) throws Exception{
        Class<?> type = parameter.getType();
        if (type.isInterface()){
            throw new RequestParameterException("参数是一个接口。无法进行赋值");
        }
        Object targetObject = type.newInstance();
        for (Field field : targetObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (List.class.isAssignableFrom(field.getType())){
                //如果是一个List集合
                Map.Entry<String, Object> list = list(field.getName(), request);
                field.set(targetObject,list.getValue());
            }else if (ClassUtils.isWrapClass(field.getType())){
                field.set(targetObject,basic(field.getType(),request.getParameter(field.getName())));
            }
        }
        return new AbstractMap.SimpleEntry<>(parameter.getName(),targetObject);
    }

    private Map.Entry<String,Object> map(HttpServletRequest request,Parameter parameter){
        Map<String,Object> map = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String key = parameterNames.nextElement();
            String[] values = request.getParameterValues(key);
            if (!ObjectUtils.isEmpty(values)){
                if (values.length > 1){
                    map.put(key,values);
                }else {
                    map.put(key,values[0]);
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(parameter.getName(),map);
    }

    private Map.Entry<String,Object> list(String key,HttpServletRequest request){
        String[] values = request.getParameterValues(key);
        List<String> list = new ArrayList<String>();
        if (!ObjectUtils.isEmpty(values)){
            for (String value : values) {
                list.add(value);
            }
        }
        return new AbstractMap.SimpleEntry<>(key,list);
    }

    private Object basic(Class<?> clazz,String value){
        return StringUtils.basicType(clazz,value);
    }

}
