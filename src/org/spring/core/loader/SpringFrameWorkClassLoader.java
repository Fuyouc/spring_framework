package org.spring.core.loader;

import org.spring.Application;
import org.spring.annotations.autoconfig.AutoConfig;
import org.spring.annotations.handler.loader.SpringConfigurationAnnotationHandler;
import org.spring.core.container.bean.BeanFactory;
import org.spring.core.handler.SpringBeanCandidateAfterClassHandler;
import org.spring.core.handler.SpringBeanCandidateBeforeClassHandler;
import org.spring.core.handler.SpringBeanClassLoaderHandler;
import org.spring.core.init.InitializingBean;
import org.spring.utils.global.ClassUtils;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;

import java.util.*;

/**
 * 类加载器
 */
public final class SpringFrameWorkClassLoader {

    private static List<Class<?>> classes = new LinkedList<>();

    public static void loadClass(Class<?> targetClass){
        /**
         * 如果扫描到的是一个注解或者该类上没有注解，则不进行加载
         */
        if (targetClass.isAnnotation() || targetClass.getAnnotations().length == 0) return;

        if (targetClass.getAnnotation(AutoConfig.class) != null){
            loadAutoConfigClass(targetClass);
        }else {
            classes.add(targetClass);
        }
    }

    private static void loadAutoConfigClass(Class<?> targetClass){
        AutoConfig config = targetClass.getAnnotation(AutoConfig.class);
        String name = config.value();
        if (StringUtils.isEmpty(name)) name = targetClass.getName();
        Object object = ClassUtils.object(targetClass);
        Application.getApplicationContext().getFactory().getBeanFactory().putBean(name, object);
    }

    public static void classLoad(){
        BeanFactory beanFactory = Application.getApplicationContext().getFactory().getBeanFactory();
        List<SpringBeanClassLoaderHandler> beanClassLoaderHandlers = beanFactory.getInterfaceBYList(SpringBeanClassLoaderHandler.class);
        //记录下其他需要被处理的类
        List<Class<?>> candidateBeanClass = new LinkedList<>();
        boolean flag = false;
        /**
         * 加载所有标记注入型注解的bean
         */
        for (Class<?> clazz : classes) {
            classLoad:for (SpringBeanClassLoaderHandler beanClassLoaderHandler : beanClassLoaderHandlers) {
                if (beanClassLoaderHandler.handler(clazz)) {
                    flag = true;
                    break classLoad;
                }
            }
            if (!flag){
                candidateBeanClass.add(clazz);
            }
            flag = false;
        }

        /**
         * 加载所有 configuration 中@bean被@ConditionalOnMissingBean标志的bean
         */
        SpringConfigurationAnnotationHandler.loadConditionalOnMissingBean();


        List<SpringBeanCandidateBeforeClassHandler> beforeClassHandlers = Application.getApplicationContext().getFactory().getBeanFactory().getInterfaceBYList(SpringBeanCandidateBeforeClassHandler.class);
        for (Class<?> candidateClass : candidateBeanClass) {
            for (SpringBeanCandidateBeforeClassHandler candidateClassLoaderHandler : beforeClassHandlers) {
                candidateClassLoaderHandler.handler(candidateClass);
            }
        }

        List<SpringBeanCandidateAfterClassHandler> afterClassHandlers = Application.getApplicationContext().getFactory().getBeanFactory().getInterfaceBYList(SpringBeanCandidateAfterClassHandler.class);
        for (Class<?> candidateClass : candidateBeanClass) {
            for (SpringBeanCandidateAfterClassHandler candidateClassLoaderHandler : afterClassHandlers) {
                candidateClassLoaderHandler.handler(candidateClass);
            }
        }

        /**
         * 加载所有 configuration 中@bean被@Conditional标志的bean
         */
        SpringConfigurationAnnotationHandler.loadConditionalBean();

        classes.clear();
        candidateBeanClass.clear();

        //获取所有的bean，校验是否需要进行初始化
        for (Map.Entry<String, Object> entry : beanFactory.getBeanList()) {
            if (InitializingBean.class.isInstance(entry.getValue())){
                ((InitializingBean)entry.getValue()).init();
            }
        }
    }

}
