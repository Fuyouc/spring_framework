package org.spring.web.annotations.handler;

import com.sun.nio.sctp.HandlerResult;
import org.spring.Application;
import org.spring.annotations.autoconfig.AutoConfig;
import org.spring.annotations.autoconfig.Autowired;
import org.spring.core.handler.SpringBeanClassHandler;
import org.spring.core.handler.SpringBeanClassLoaderHandler;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.StringUtils;
import org.spring.web.annotations.ControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@AutoConfig
public class SpringWebControllerAdviceAnnotationHandler implements SpringBeanClassLoaderHandler {

    private static List<Object> exceptionHandlers = new ArrayList<>();

    @Override
    public boolean handler(Class<?> objClass) {
        ControllerAdvice advice = objClass.getAnnotation(ControllerAdvice.class);
        if (advice == null) return false;
        Object obj = ClassUtils.object(objClass);
        String name = advice.value();
        name = StringUtils.isEmpty(name) ? objClass.getName() : name;
        Application.getApplicationContext().getFactory().getBeanFactory().putBean(name,obj);
        exceptionHandlers.add(obj);
        return true;
    }

    public static List<Object> getExceptionHandler(){
        return exceptionHandlers;
    }
}
