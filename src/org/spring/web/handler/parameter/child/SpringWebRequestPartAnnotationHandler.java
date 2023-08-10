package org.spring.web.handler.parameter.child;

import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;
import org.spring.web.annotations.request.parameter.RequestPart;
import org.spring.web.exception.HttpRequestMethodNotSupportedException;
import org.spring.web.exception.RequestParameterException;
import org.spring.web.file.MultipartFile;
import org.spring.web.handler.parameter.SpringWebRequestParameterHandler;
import org.spring.web.handler.parameter.post.PostFormDataHandler;
import org.spring.web.handler.parameter.post.PostURIEncodedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@Component
public class SpringWebRequestPartAnnotationHandler implements SpringWebRequestParameterHandler {

    @Autowired
    private PostURIEncodedHandler uriEncodedHandler;

    @Autowired
    private PostFormDataHandler formDataHandler;

    @Override
    public Map.Entry<Boolean,Object> handler(HttpServletRequest request, HttpServletResponse response, Method method, Parameter parameter, Map<String,Object> cache) throws Exception{
        if (!request.getMethod().equals("POST")) throw new HttpRequestMethodNotSupportedException("不是一个POST请求，无法获取@RequestPart的参数");
        if (request.getContentType() == null || request.getContentType().startsWith("application/json")) return null;


        Map<String,Object> body = null;
        if (cache.isEmpty()){
            //如果缓存为空，则从请求体中获取数据
            if (uriEncodedHandler.allow(request)){
                body = uriEncodedHandler.handler(request,method,parameter).getValue();
            }else if (formDataHandler.allow(request)){
                body = formDataHandler.handler(request,method,parameter).getValue();
            }
            if (body != null) {
                cache.putAll(body);
            }
        }else {
            //否则使用缓存的内容
            body = cache;
        }
        RequestPart part = parameter.getAnnotation(RequestPart.class);
        String key = StringUtils.isEmpty(part.value()) ? parameter.getName() : part.value();
        Object result = null;
        Class<?> type = parameter.getType();
        if (ClassUtils.isWrapClass(type)){
            //普通类型的数据
            result = basic(parameter,body.get(key));
        }else if (Map.class.isAssignableFrom(type)){
            result = body;
        }else if (MultipartFile.class.isAssignableFrom(type)){
            //如果是一个单文件类型
            if (body.containsKey(key)){
                List<MultipartFile> multipartFiles = (List<MultipartFile>) body.get(key);
                result = multipartFiles.size() == 0 ? null : multipartFiles.get(0);
            }
        }else if (List.class.isAssignableFrom(type)){
            //处理List类型
            result = body.get(key);
        }else {
            //处理的是一个对象类型
            result = object(body,type);
        }
        if (!ObjectUtils.isEmpty(part) && !part.required() && ObjectUtils.isEmpty(request)) throw new RequestParameterException("parameter " + key + " is null");
        return new AbstractMap.SimpleEntry<>(true,result);
    }

    @Override
    public boolean allow(Parameter parameter) {
        return !ObjectUtils.isEmpty(parameter.getAnnotation(RequestPart.class));
    }

    private Object basic(Parameter parameter,Object value){
        return StringUtils.basicType(parameter.getType(), (String) value);
    }

    private Object object(Map<String,Object> body,Class<?> targetClass) throws IllegalAccessException, InstantiationException {
        Object target = targetClass.newInstance();
        for (Field field : targetClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (ClassUtils.isWrapClass(field.getType())){
                Object value = body.get(field.getName());
                if (!ObjectUtils.isEmpty(value)) {
                    field.set(target, StringUtils.basicType(field.getType(), (String) value));
                }
            }else if (MultipartFile.class.isAssignableFrom(field.getType())){
                List<MultipartFile> files = (List<MultipartFile>) body.get(field.getName());
                if (!ObjectUtils.isEmpty(files)){
                    field.set(target,files.get(0));
                }
            }else if (List.class.isAssignableFrom(field.getType())){
                field.set(target,body.get(field));
            }
        }
        return target;
    }


}
