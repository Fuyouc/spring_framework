package org.spring.annotations.handler.loader;


import org.spring.Application;
import org.spring.annotations.autoconfig.AutoConfig;
import org.spring.annotations.autoconfig.Controller;
import org.spring.core.handler.SpringBeanClassLoaderHandler;
import org.spring.utils.global.ClassUtils;

@AutoConfig
public class SpringControllerAnnotationHandler implements SpringBeanClassLoaderHandler {

    @Override
    public boolean handler(Class<?> objClass) {
        Controller service = objClass.getAnnotation(Controller.class);
        if (service == null) return false;
        Object object = ClassUtils.object(objClass);
        Application.getApplicationContext().getFactory().getBeanFactory().putBean(service.name(), object);
        return true;
    }
}
