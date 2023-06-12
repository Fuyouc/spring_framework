package org.spring.annotations.handler.loader;


import org.spring.Application;
import org.spring.annotations.autoconfig.AutoConfig;
import org.spring.annotations.autoconfig.Service;
import org.spring.core.handler.SpringBeanClassLoaderHandler;
import org.spring.utils.global.ClassUtils;

@AutoConfig
public class SpringServiceAnnotationHandler implements SpringBeanClassLoaderHandler {
    @Override
    public boolean handler(Class<?> objClass) {
        Service service = objClass.getAnnotation(Service.class);
        if (service == null) return false;
        Object object = ClassUtils.object(objClass);
        Application.getApplicationContext().getFactory().getBeanFactory().putBean(service.value(),object);
        return true;
    }
}
