package org.spring.jdbc.sql.execute.handler.dynamic.child;

import org.spring.annotations.autoconfig.Component;
import org.spring.core.parser.xml.Element;
import org.spring.jdbc.annotation.Param;
import org.spring.jdbc.sql.DaoMethod;
import org.spring.jdbc.sql.execute.handler.dynamic.AbstractDynamicHandler;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;

import javax.management.modelmbean.XMLParseException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Foreach标签处理器
 * Foreach允许动态的批量添加语句（如：批量插入、批量查找等）
 */
@Component
public class DynamicForeachHandler extends AbstractDynamicHandler {
    @Override
    public void dynamicSQL(DaoMethod daoMethod) throws Exception {
        Element root = daoMethod.getRoot();
        List<Element> foreachChild = root.getChild("foreach");
        if (!ObjectUtils.isEmpty(foreachChild)){
            StringBuilder sb = new StringBuilder();
            for (Element element : foreachChild) {
                sb.setLength(0);
                //获取属性列表
                String id = element.getAttributes("collection");
                String item = element.getAttributes("item");
                String open = StringUtils.getValue(element.getAttributes("open"),"(");
                String close = StringUtils.getValue(element.getAttributes("close"),")");
                String separator = StringUtils.getValue(element.getAttributes("separator"),",");
                /**
                 * 获取集合
                 */
                Collection collection = getCollection(daoMethod, id);
                if (!ObjectUtils.isEmpty(collection)){
                    Map<String, Queue<Object>> mapperParam = new HashMap<>();
                    sb.append(open);
                    /**
                     * 处理集合中的类型
                     * 如果是Map或者Object类型，则会在采用 item.key 来作为唯一标识
                     * mapperParam中的value每个都是一个队列
                     * 利用队列的先进先出，可以保证在进行SQL映射时，参数是有序的
                     */
                    for (Object value : collection) {
                        sb.append(element.buildContext() + separator);
                        if (Map.class.isInstance(value)){
                            map(item,mapperParam, (Map<String, Object>) value);
                        }else if (!ClassUtils.isWrapClass(value.getClass())){
                            //对象类型
                            object(item,mapperParam,value);
                        }else if (ClassUtils.isWrapClass(value.getClass())){
                            //基本类型
                            basic(item,mapperParam,value);
                        }
                    }
                    if (collection.size() > 1){
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append(close);
                    root.addContext(sb.toString());
                    daoMethod.setMapperParam(mapperParam);
                }else {
                    throw new XMLParseException("在参数列表中找不到名为[" + id + "]的集合");
                }
            }
        }
    }

    private void map(String id,Map<String, Queue<Object>> mapperParam,Map<String,Object> value){
        String key = null;
        Iterator<Map.Entry<String, Object>> iterator = value.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> entry = iterator.next();
            key = id + "." + entry.getKey();
            Queue<Object> objectQueue = mapperParam.get(key);
            if (ObjectUtils.isEmpty(objectQueue)){
                objectQueue = new LinkedList<>();
                mapperParam.put(key,objectQueue);
            }
            objectQueue.add(entry.getValue());
        }
    }

    private void object(String id,Map<String, Queue<Object>> mapperParam,Object value){
        try {
            Class<?> clazz = value.getClass();
            String key = null;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                key = id + "." + field.getName();
                Queue<Object> objectQueue = mapperParam.get(key);
                if (ObjectUtils.isEmpty(objectQueue)){
                    objectQueue = new LinkedList<>();
                    mapperParam.put(key,objectQueue);
                }
                objectQueue.add(field.get(value));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void basic(String id,Map<String, Queue<Object>> mapperParam,Object value){
        Queue<Object> objectQueue = mapperParam.get(id);
        if (ObjectUtils.isEmpty(objectQueue)){
            objectQueue = new LinkedList<>();
            mapperParam.put(id,objectQueue);
        }
        objectQueue.add(value);
    }

    private Collection getCollection(DaoMethod daoMethod,String id){
        Method method = daoMethod.getMethod();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String name = ObjectUtils.isEmpty(parameter.getAnnotation(Param.class)) ? parameter.getName() : parameter.getAnnotation(Param.class).value();
            if (name.equals(id)){
                return (Collection) daoMethod.getValue(i);
            }
        }
        return null;
    }
}
