package org.spring.annotations.handler.loader;

import org.spring.Application;
import org.spring.annotations.Conditional;
import org.spring.annotations.ConditionalOnMissingBean;
import org.spring.annotations.autoconfig.AutoConfig;
import org.spring.annotations.autoconfig.Bean;
import org.spring.annotations.autoconfig.Configuration;
import org.spring.annotations.handler.conditional.after.SpringConditionalAnnotationClassHandler;
import org.spring.annotations.handler.conditional.before.SpringConditionalOnMissingBeanAnnotationClassHandler;
import org.spring.core.container.bean.BeanFactory;
import org.spring.core.handler.SpringBeanClassLoaderHandler;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

@AutoConfig
public class SpringConfigurationAnnotationHandler implements SpringBeanClassLoaderHandler {

    private static Map<Object,List<Method>> conditionalOnMissingBean = new HashMap<>();

    private static Map<Object,List<Method>> conditionalBean = new HashMap<>();

    @Override
    public boolean handler(Class<?> beanClass) {
        if (!ObjectUtils.isEmpty(beanClass.getAnnotation(Configuration.class))){
            Configuration configuration = beanClass.getAnnotation(Configuration.class);
            BeanFactory beanFactory = Application.getApplicationContext().getFactory().getBeanFactory();
            Object configurationObj = ClassUtils.object(beanClass);
            if (configuration.isEnable()){
                //如果配置类开启该属性，才向容器中注入对象
                beanFactory.putBean(configuration.value(), configurationObj);
            }
            for (Method method : beanClass.getDeclaredMethods()) {
                method.setAccessible(true);
                if (!ObjectUtils.isEmpty(method.getAnnotation(Bean.class))){
                    //如果方法上只有一个 Bean，则直接注入到容器中
                    if (ObjectUtils.isEmpty(method.getAnnotation(Conditional.class))
                            && ObjectUtils.isEmpty(method.getAnnotation(ConditionalOnMissingBean.class))){
                        String name = method.getAnnotation(Bean.class).value();
                        name = StringUtils.isEmpty(name) ? method.getName() : name;
                        try {
                            beanFactory.putBean(name,method.invoke(configurationObj,null));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        if (!ObjectUtils.isEmpty(method.getAnnotation(Conditional.class))){
                            addMethod(conditionalBean,configurationObj,method);
                        }else {
                            addMethod(conditionalOnMissingBean,configurationObj,method);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void addMethod(Map<Object,List<Method>> map,Object target,Method method){
        if (!map.containsKey(target)){
            List<Method> list = new LinkedList<>();
            list.add(method);
            map.put(target,list);
        }else {
            map.get(target).add(method);
        }
    }

    public static void loadConditionalOnMissingBean(){
        Iterator<Map.Entry<Object, List<Method>>> iterator = conditionalOnMissingBean.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Object, List<Method>> entry = iterator.next();
            for (Method method : entry.getValue()) {
                String name = method.getAnnotation(Bean.class).value();
                name = StringUtils.isEmpty(name) ? method.getName() : name;
                conditionalOnMissingBean(name,method,entry.getKey());
            }
        }
        //清空集合，避免占用内存
        conditionalOnMissingBean.clear();
    }

    public static void loadConditionalBean(){
        Iterator<Map.Entry<Object, List<Method>>> iterator = conditionalBean.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Object, List<Method>> entry = iterator.next();
            for (Method method : entry.getValue()) {
                String name = method.getAnnotation(Bean.class).value();
                name = StringUtils.isEmpty(name) ? method.getName() : name;
                conditionalBean(name,method,entry.getKey());
            }
        }
        //清空集合，避免占用内存
        conditionalBean.clear();
    }

    private static void conditionalOnMissingBean(String name,Method method,Object target){
        SpringConditionalOnMissingBeanAnnotationClassHandler.beanMethod(name,method.getAnnotation(ConditionalOnMissingBean.class),method,target);
    }

    private static void conditionalBean(String name,Method method,Object target){
        SpringConditionalAnnotationClassHandler.beanMethod(name,method.getAnnotation(Conditional.class),method,target);
    }
}
