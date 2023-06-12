package org.spring.core.handler;

/**
 * 类加载处理器（扫描所有包下的class文件，并交该处理器进行处理）
 */
public interface SpringBeanClassLoaderHandler {
    /**
     * 如果处理器接收该请求处理，则返回true，否则返回false
     */
    boolean handler(Class<?> targetClass);
}
