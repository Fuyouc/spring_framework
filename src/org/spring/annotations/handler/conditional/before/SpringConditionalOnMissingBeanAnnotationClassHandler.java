package org.spring.annotations.handler.conditional.before;

import org.spring.Application;
import org.spring.annotations.ConditionalOnMissingBean;
import org.spring.annotations.autoconfig.Component;
import org.spring.core.container.bean.BeanFactory;
import org.spring.core.handler.SpringBeanCandidateBeforeClassHandler;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class SpringConditionalOnMissingBeanAnnotationClassHandler implements SpringBeanCandidateBeforeClassHandler {
    @Override
    public void handler(Class<?> beanClass) {
        if (!ObjectUtils.isEmpty(beanClass.getAnnotation(ConditionalOnMissingBean.class))){
            beanClass(beanClass.getName(),beanClass.getAnnotation(ConditionalOnMissingBean.class),beanClass);
        }
    }

    public static void beanClass(String name,ConditionalOnMissingBean conditional,Class<?> clazz){
        if (conditions(conditional)){
            Application.getApplicationContext().getFactory().getBeanFactory().putBean(name, ClassUtils.object(clazz));
        }
    }

    public static void beanMethod(String name, ConditionalOnMissingBean conditional, Method method,Object target){
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

    private static boolean conditions(ConditionalOnMissingBean conditional){
        BeanFactory beanFactory = Application.getApplicationContext().getFactory().getBeanFactory();
        String[] name = conditional.name();
        Class<?>[] value = conditional.value();
        if (!ObjectUtils.isEmpty(name)){
            if (!beanFactory.notContainsAll(name)) return false;
        }
        if (!ObjectUtils.isEmpty(value)){
            if (!beanFactory.notContainsAll(value)) return false;
        }
        return true;
    }

}
