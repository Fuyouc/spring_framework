package org.spring.annotations.handler.loader;

import org.spring.Application;
import org.spring.annotations.autoconfig.AutoConfig;
import org.spring.annotations.autoconfig.Component;
import org.spring.core.handler.SpringBeanClassLoaderHandler;
import org.spring.utils.global.ClassUtils;

@AutoConfig
public class SpringComponentAnnotationHandler implements SpringBeanClassLoaderHandler {
    @Override
    public boolean handler(Class<?> targetClass) {
        Component annotation = targetClass.getAnnotation(Component.class); //获取注解
        if (annotation != null){
            Object object = ClassUtils.object(targetClass);
            Application.getApplicationContext().getFactory().getBeanFactory().putBean(annotation.value(), object);
            return true;
        }
        return false;
    }
}
