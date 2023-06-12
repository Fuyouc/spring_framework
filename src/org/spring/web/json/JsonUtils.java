package org.spring.web.json;

import org.spring.annotations.NotJson;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class JsonUtils {

    public static String toString(Object value){
        if (ObjectUtils.isEmpty(value)) return null;
        if (ClassUtils.isWrapClass(value.getClass())) return String.valueOf(value);
        Class<?> type = value.getClass();
        if (Map.class.isAssignableFrom(type)){
            return map(value);
        }else if (List.class.isAssignableFrom(type)){
            return list(value);
        }else if (!ClassUtils.isWrapClass(type)){
            return object(value);
        }
        return null;
    }

    private static String basic(String key,Object value){
        if (!ObjectUtils.isEmpty(value)){
            String result = null;
            if (value instanceof String || value instanceof Character){
                result = ("\"" + value + "\"");
            }else {
                result = String.valueOf(value);
            }
            return ("\"" + key + "\"" + ":" + result);
        }
        return ("\"" + key + "\"" + ":" + "null");
    }

    private static String object(Object object){
        try {
            Class<?> clazz = object.getClass();
            StringBuilder sb = new StringBuilder("{\n");
            Field[] fields = clazz.getDeclaredFields(); //获取所有字段
            for (Field field : fields) {
                if (field.getAnnotation(NotJson.class) == null) {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    tow:
                    if (!ClassUtils.isWrapClass(field.getType())) {
                        //如果是一个对象类型
                        sb.append("\"" + field.getName() + "\"" + ":" +  toString(value) + ",\n");
                    } else {
                        sb.append(basic(field.getName(),value) + ",\n");
                    }
                }
            }
            sb.append("}");
            if (fields.length > 0) {
                sb.deleteCharAt(sb.lastIndexOf(","));
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static String map(Object target){
        StringBuilder sb = new StringBuilder("{\n");
        Map<String,Object> map = (Map<String, Object>) target;
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            tow:
            if (!ClassUtils.isWrapClass(value.getClass())) {
                sb.append("\"" + key + "\"" + ":" +  toString(value) + ",\n");
            } else {
                sb.append(basic(key,value) + ",\n");
            }
        }
        sb.append("}");
        if (map.size() > 0) {
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        return sb.toString();
    }

    private static String list(Object value){
        StringBuilder sb = new StringBuilder("[\n");
        List<Object> list = (List<Object>) value;
        for (int i = 0; i < list.size(); i++) {
            Object targetObject = list.get(i);
            if (Map.class.isAssignableFrom(targetObject.getClass())){
                sb.append(map(targetObject) + ",\n");
            }else if (!ClassUtils.isWrapClass(targetObject.getClass())){
                sb.append(object(targetObject) + ",\n");
            }else {
                sb.append(targetObject + ",\n");
            }
        }
        sb.append("]");
        if (list.size() != 0) {
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        return sb.toString();
    }
}
