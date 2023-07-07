package org.spring.web.handler.parameter.child;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.spring.annotations.autoconfig.Component;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;
import org.spring.web.annotations.request.parameter.RequestBody;
import org.spring.web.exception.HttpRequestMethodNotSupportedException;
import org.spring.web.handler.parameter.SpringWebRequestParameterHandler;
import org.spring.web.server.servlet.SpringWebServletRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.*;
import java.util.*;

@Component
public class SpringWebRequestBodyAnnotationHandler implements SpringWebRequestParameterHandler {

    @Override
    public Map.Entry<Boolean, Object> handler(HttpServletRequest request, HttpServletResponse response, Method method, Parameter parameter, Map<String, Object> cache) throws Exception {
        if (!request.getMethod().equals("POST")) throw new HttpRequestMethodNotSupportedException("不是一个POST请求，无法获取@RequestBody的参数");
        if (request.getContentType() == null || !request.getContentType().startsWith("application/json")) return null;
        if (cache.isEmpty()){
            cache.put("json",((SpringWebServletRequestWrapper)request).getBodyString().trim());
        }
        String json = (String) cache.get("json");
        Object result = null;
        if (json != null && !"".equals(json)) {
            if ('{' == json.charAt(0)) {
                JSONObject jsonObject = null;
                if (!cache.containsKey("object")) {
                    jsonObject = JSONObject.parseObject(json);
                    cache.put("object", jsonObject);
                } else {
                    jsonObject = (JSONObject) cache.get("object");
                }
                result = jsonObject(jsonObject, parameter);
            } else if ('[' == json.charAt(0)) {
                JSONArray jsonArray = null;
                if (!cache.containsKey("array")) {
                    jsonArray = JSONArray.parseArray(json);
                    cache.put("array", jsonArray);
                } else {
                    jsonArray = (JSONArray) cache.get("array");
                }
                result = jsonArray(jsonArray, parameter);
            }
        }
        return new AbstractMap.SimpleEntry<>(true,result);
    }

    /**
     * 处理单json数据
     */
    private Object jsonObject(JSONObject jsonObject,Parameter parameter) throws Exception {
        Class<?> type = parameter.getType();
        if (Map.class.isAssignableFrom(type)){
            return map(jsonObject);
        }else {
            return object(parameter.getType(),jsonObject);
        }
    }

    /**
     * 处理json数组数据
     */
    private Object jsonArray(JSONArray jsonArray,Parameter parameter) throws Exception {
        if (List.class.isAssignableFrom(parameter.getType()) && parameter.getParameterizedType() instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
            if (parameterizedType != null) {
                Type elementType = parameterizedType.getActualTypeArguments()[0];
                if (elementType instanceof Class){
                    return list((Class<?>) elementType,jsonArray);
                }else {
                    return map(jsonArray);
                }
            }
        }
        return null;
    }

    //如果目标为实体类
    private Object object(Class<?> clazz,JSONObject jsonObject) throws Exception{
        Object target = clazz.newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = jsonObject.get(field.getName());
            if (value instanceof JSONObject){
                JSONObject valueJsonObject = (JSONObject) value;
                field.set(target,object(field.getType(),valueJsonObject));
            }else if (value instanceof JSONArray){
                JSONArray valueJsonObject = (JSONArray) value;
                // 获取该字段的类型
                Type type = field.getGenericType();
                // 判断该字段是否为参数化类型（即泛型）
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    // 获取参数化类型中的实际类型（即 List 中的元素类型）
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    // 取出数组中第一个元素（即 List 中的元素类型）
                    Type elementType = actualTypeArguments[0];
                    field.set(target,list((Class<?>) elementType,valueJsonObject));
                }
            }else {
                if (!ObjectUtils.isEmpty(value)) {
                    field.set(target, StringUtils.basicType(field.getType(), String.valueOf(value)));
                }
            }
        }
        return target;
    }

    //如果目标为List<实体类>
    private List list(Class<?> clazz,JSONArray jsonArray) throws Exception{
        List list = new ArrayList();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            list.add(object(clazz,jsonObject));
        }
        return list;
    }

    private Object map(Object json){
        Map<String,Object> map = new HashMap<>();
        if (json instanceof JSONObject){
            JSONObject jsonObject = (JSONObject) json;
            Iterator<String> iterator = jsonObject.keySet().iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject || value instanceof JSONArray){
                    map.put(key,map(value));
                }else {
                    map.put(key,value);
                }
            }
            return map;
        }else if (json instanceof JSONArray){
            List list = new ArrayList();
            JSONArray jsonArray = (JSONArray) json;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (!ObjectUtils.isEmpty(jsonObject)){
                    list.add(map(jsonObject));
                }else {
                    list.add(jsonArray.get(i));
                }
            }
            return list;
        }
        return null;
    }


    @Override
    public boolean allow(Parameter parameter) {
        return !ObjectUtils.isEmpty(parameter.getAnnotation(RequestBody.class));
    }
}
