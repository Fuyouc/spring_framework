package org.spring.web.annotations.handler;

import com.sun.nio.sctp.HandlerResult;
import org.spring.Application;
import org.spring.annotations.autoconfig.AutoConfig;
import org.spring.annotations.autoconfig.Autowired;
import org.spring.annotations.autoconfig.Component;
import org.spring.core.handler.SpringBeanClassHandler;
import org.spring.core.handler.SpringBeanClassLoaderHandler;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.StringUtils;
import org.spring.web.annotations.ControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpringWebControllerAdviceAnnotationHandler implements SpringBeanClassHandler {

    private static List<Object> exceptionHandlers = new ArrayList<>();

    public static List<Object> getExceptionHandler(){
        return exceptionHandlers;
    }

    @Override
    public void handler(String beanName, Object bean, Class<?> beanClass) {
        ControllerAdvice advice = beanClass.getAnnotation(ControllerAdvice.class);
        if (advice == null) return;
        exceptionHandlers.add(bean);
    }
}
