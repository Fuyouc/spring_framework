package org.spring.core.handler;

import java.lang.reflect.Field;

/**
 * 处理容器中所有的bean的字段
 */
public interface SpringBeanFieldHandler {
    void handler(String beanName, Object bean,Field field);
}