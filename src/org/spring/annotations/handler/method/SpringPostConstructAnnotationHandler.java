package org.spring.annotations.handler.method;

import org.spring.Application;
import org.spring.annotations.PostConstruct;
import org.spring.annotations.autoconfig.Component;
import org.spring.core.container.bean.BeanFactory;
import org.spring.core.handler.SpringBeanMethodHandler;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SpringPostConstructAnnotationHandler implements SpringBeanMethodHandler {
    @Override
    public void handler(String beanName, Object bean, Method method) {
        if (ObjectUtils.isEmpty(method.getAnnotation(PostConstruct.class))) return;
        method.setAccessible(true);
        try {
            if (method.getParameterCount() > 0){
                //获取参数列表
                Parameter[] parameters = method.getParameters();
                Object[] paramValues = new Object[parameters.length];
                int index = 0;
                Object value = null;
                BeanFactory beanFactory = Application.getApplicationContext().getFactory().getBeanFactory();
                for (Parameter param : parameters) {
                    if (List.class.isAssignableFrom(param.getType())){
                        value = list(param,beanFactory);
                    }else if (!ClassUtils.isWrapClass(param.getType())){
                        value = object(param,beanFactory);
                    }
                    paramValues[index++] = value;
                    value = null;
                }
                if (parameters.length > 1){
                    method.invoke(bean,paramValues);
                }else {
                    method.invoke(bean,paramValues[0]);
                }
                return;
            }
            method.invoke(bean,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //如果参数中需要的一个List对象
    private List list(Parameter parameter,BeanFactory beanFactory){
        if (parameter.getParameterizedType() instanceof ParameterizedType){
            List list = new ArrayList();
            ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
            if (parameterizedType != null) {
                Type elementType = parameterizedType.getActualTypeArguments()[0];
                Class<?> clazz = (Class<?>) elementType;
                List<Map.Entry<String,Object>> loadClassObj = Application.getApplicationContext().getFactory().getBeanFactory().getBeanList();
                for (int i = 0; i < loadClassObj.size(); i++) {
                    Object value = loadClassObj.get(i).getValue();
                    if (clazz.isInstance(value)){
                        //如果容器中存在该类的具体实现
                        list.add(value);
                    }
                }
            }
            return list;
        }
        return null;
    }

    private Object object(Parameter param,BeanFactory beanFactory){
        Object value = beanFactory.getBean(param.getName());
        if (ObjectUtils.isEmpty(value)){
            value = beanFactory.getBean(param.getType());
        }
        return value;
    }
}
