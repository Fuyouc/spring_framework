package org.spring.core.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 处理所有bean中所有的方法
 */
public interface SpringBeanMethodHandler {
    void handler(String beanName, Object bean, Method method);
}