package org.spring.core.container.bean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Bean工厂
 */
public interface BeanFactory{
    /**
     * 添加 bean
     */
    void putBean(String beanName,Object bean);

    /**
     * 根据beanName删除bean
     */
    void removeBean(String beanName);


    void removeBean(Class<?> beanClass);

    /**
     * 判断容器中是否有以 beanName 命名的bean
     */
    boolean contains(String beanName);

    /**
     * 判断容器中是否有 beanClass 类型的bean
     */
    boolean contains(Class<?> beanClass);

    /**
     * 容器中包含所有的beanName对象
     */
    boolean containsAll(String[] beanName);

    boolean notContainsAll(String[] beanName);

    /**
     * 容器中包含beanClass所有对象
     */
    boolean containsAll(Class<?>[] beanClass);

    /**
     * 容器中不包含beanClass所有对象
     */
    boolean notContainsAll(Class<?>[] beanClass);

    /**
     * 根据beanName获取Bean
     */
    <T> T getBean(String beanName);

    /**
     * 获取指定 class 类型的对象
     */
    <T> T getBean(Class<?> beanClass);

    /**
     * 以Map的形势获取所有Bean
     */
    Map<String, Object> getBeanMap();

    /**
     * 以List的形势获取所有Bean
     * @return
     */
    List<Map.Entry<String,Object>> getBeanList();

    /**
     * 获取指定接口class的所有实现类（以Map的形势返回）
     */
    <T> Map<String,T> getInterfaceBYMap(Class<T> interfaceClass);

    /**
     * 获取指定接口class的所有实现类（以List的形势返回）
     */
    <T> List<T> getInterfaceBYList(Class<T> interfaceClass);
}
