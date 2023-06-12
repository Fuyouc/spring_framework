package org.spring.annotations.handler.conditional.after;

import org.spring.Application;
import org.spring.annotations.Conditional;
import org.spring.annotations.ConditionalOnMissingBean;
import org.spring.annotations.autoconfig.Component;
import org.spring.core.container.bean.BeanFactory;
import org.spring.core.handler.SpringBeanCandidateAfterClassHandler;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class SpringConditionalAnnotationClassHandler implements SpringBeanCandidateAfterClassHandler {
    @Override
    public void handler(Class<?> beanClass) {
        if (!ObjectUtils.isEmpty(beanClass.getAnnotation(Conditional.class))){
            beanClass(beanClass.getName(),beanClass.getAnnotation(Conditional.class),beanClass);
        }
    }

    public static void beanClass(String name,Conditional conditional,Class<?> clazz){
        if (conditions(conditional)){
            Application.getApplicationContext().getFactory().getBeanFactory().putBean(name, ClassUtils.object(clazz));
        }
    }

    public static void beanMethod(String name, Conditional conditional, Method method, Object target){
        if (conditions(conditional)){
            try {
                Application.getApplicationContext().getFactory().getBeanFactory().putBean(name, method.invoke(target,null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean conditions(Conditional conditional){
        BeanFactory beanFactory = Application.getApplicationContext().getFactory().getBeanFactory();
        String[] name = conditional.name();
        Class<?>[] value = conditional.value();
        if (!ObjectUtils.isEmpty(name)){
            if (!beanFactory.containsAll(name)) return false;
        }
        if (!ObjectUtils.isEmpty(value)){
            if (!beanFactory.containsAll(value)) return false;
        }
        return true;
    }

}
