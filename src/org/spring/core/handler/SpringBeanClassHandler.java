package org.spring.core.handler;

/**
 * 处理容器中所有的bean
 */
public interface SpringBeanClassHandler {
    void handler(String beanName,Object bean,Class<?> beanClass);
}