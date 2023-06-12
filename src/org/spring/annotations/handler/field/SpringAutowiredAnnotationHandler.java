package org.spring.annotations.handler.field;

import org.spring.Application;
import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.core.handler.SpringBeanFieldHandler;
import org.spring.utils.global.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpringAutowiredAnnotationHandler implements SpringBeanFieldHandler {

    @Override
    public void handler(String beanName, Object bean, Field field) {
        Autowired annotation = field.getAnnotation(Autowired.class);
        if (annotation != null){
            field.setAccessible(true);
            String key = annotation.value();
            try {
                if (List.class.isAssignableFrom(field.getType())){
                    filedList(field,bean);
                }else if (Map.class.isAssignableFrom(field.getType())){
                    filedMap(field,bean);
                }else {
                    filedObject(key,field,bean);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 字段是List类型
     */
    private void filedList(Field field,Object target) throws Exception{
        List list = new ArrayList<>();
        ParameterizedType genericType = (ParameterizedType) field.getGenericType(); // 获取泛型类型
        Class<?> clazz = (Class<?>) genericType.getActualTypeArguments()[0];
        //如果类名无法找到，则根据类型注入
        List<Map.Entry<String,Object>> loadClassObj = Application.getApplicationContext().getFactory().getBeanFactory().getBeanList();
        for (int i = 0; i < loadClassObj.size(); i++) {
            Object value = loadClassObj.get(i).getValue();
            if (clazz.isInstance(value)){
                //如果容器中存在该类的具体实现
                list.add(value);
            }
        }
        field.set(target,list);
    }

    private void filedMap(Field field,Object target) throws Exception{
        Map map = new HashMap();
        List<Map.Entry<String, Object>> loadClass = Application.getApplicationContext().getFactory().getBeanFactory().getBeanList();
        ParameterizedType genericType = (ParameterizedType) field.getGenericType(); // 获取泛型类型
        Class<?> clazz = (Class<?>) genericType.getActualTypeArguments()[1];
        for (int i = 0; i < loadClass.size(); i++) {
            Map.Entry<String, Object> entry = loadClass.get(i);
            if (clazz.isInstance(entry.getValue())){
                //如果容器中存在该类的具体实现
                map.put(entry.getKey(),entry.getValue());
            }
        }
        field.set(target,map);
    }

    /**
     * 字段是一个Object类型
     */
    private void filedObject(String key,Field field,Object target) throws Exception{
        if (!StringUtils.isEmpty(key)){
            field.set(target,getValue(key,target,field));
        }else {
            Object mappingObj = Application.getApplicationContext().getFactory().getBeanFactory().getBean(field.getType().getName());
            if (mappingObj != null){
                field.set(target,mappingObj);
            }else {
                List<Map.Entry<String, Object>> beanList = Application.getApplicationContext().getFactory().getBeanFactory().getBeanList();
                for (Map.Entry<String, Object> entry : beanList) {
                    if (field.getType().isInstance(entry.getValue())){
                        field.set(target,entry.getValue());
                    }
                }
            }
        }

    }


    private Object getValue(String key,Object target,Field field){
        Object mappingObj = Application.getApplicationContext().getFactory().getBeanFactory().getBean(key);
        if (mappingObj == null){
            throw new RuntimeException(target.getClass().getName() + " 中的 "+field.getName()+" 成员变量，在容器中找不到这种类型的对象，检查value是否正确：" + key);
        }else {
            return mappingObj;
        }
    }
}
