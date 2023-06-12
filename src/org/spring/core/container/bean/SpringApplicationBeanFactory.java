package org.spring.core.container.bean;

import org.spring.utils.global.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SpringApplicationBeanFactory implements BeanFactory{

    private Map<String,Object> factory = new ConcurrentHashMap<>();

    /**
     * 注入bean
     */
    public void putBean(String beanName,Object bean){
        if (StringUtils.isEmpty(beanName)) beanName = bean.getClass().getName();
        factory.put(beanName,bean);
    }

    /**
     * 删除bean
     */
    public void removeBean(String beanName){
        factory.remove(beanName);
    }

    @Override
    public void removeBean(Class<?> beanClass) {

    }

    @Override
    public boolean contains(String beanName) {
        return factory.containsKey(beanName);
    }

    @Override
    public boolean contains(Class<?> beanClass) {
        return factory.values().stream()
                .filter(value -> beanClass.isInstance(value))
                .findFirst()
                .isPresent();
    }

    @Override
    public boolean containsAll(String[] beanName) {
        for (String key : beanName) {
            if (!factory.containsKey(key)) return false;
        }
        return true;
    }

    @Override
    public boolean notContainsAll(String[] beanName){
        for (String key : beanName) {
            if (factory.containsKey(key)) return false;
        }
        return true;
    }

    @Override
    public boolean containsAll(Class<?>[] beanClass) {
        Collection<Object> values = factory.values();
        int index = 0;
        for (int i = 0; i < beanClass.length; i++) {
            Class<?> clazz = beanClass[index];
            if (clazz.isInterface()){
                for (Object value : values) {
                    if (clazz.isInstance(value)){
                        index++;
                        break;
                    }
                }
            }else {
                for (Object value : values) {
                    if (value.getClass().isAssignableFrom(beanClass[index])){
                        index++;
                        break;
                    }
                }
            }
            if (index != (i + 1)) return false;
        }
        return true;
    }

    @Override
    public boolean notContainsAll(Class<?>[] beanClass) {
        Collection<Object> values = factory.values();
        int index = 0;
        for (int i = 0; i < beanClass.length; i++) {
            Class<?> clazz = beanClass[index];
            if (clazz.isInterface()){
                //如果该class是一个接口
                for (Object value : values){
                    //如果发现有一个类是该接口的实现类，则返回
                    if (clazz.isInstance(value)) return false;
                }
            }else {
                //否则就是一个具体的类
                for (Object value : values) {
                    /**
                     * 如果在容器中找到指定类，直接返回false
                     */
                    if (value.getClass().isAssignableFrom(clazz)) return false;
                }
            }
            ++index;
        }
        return true;
    }

    public <T> T getBean(String beanName){
        return (T) factory.get(beanName);
    }

    @Override
    public <T> T getBean(Class<?> beanClass) {
        Collection<Object> values = factory.values();
        if (beanClass.isInterface()) {
            for (Object value : values) {
                if (beanClass.isInstance(value)) return (T) value;
            }
        }else {
            for (Object value : values) {
                if (value.getClass().isAssignableFrom(beanClass)) return (T) value;
            }
        }
        return null;
    }

    public Map<String, Object> getBeanMap() {
        return factory;
    }

    /**
     * 获取bean列表
     */
    public List<Map.Entry<String,Object>> getBeanList(){
        return factory.entrySet().stream().collect(Collectors.toList());
    }

    @Override
    public <T> Map<String, T> getInterfaceBYMap(Class<T> interfaceClass) {
        Map<String,T> childMap = factory.entrySet()
                .stream()
                .filter(entry -> {
                    return interfaceClass.isAssignableFrom(entry.getValue().getClass());
                })
                .collect(Collectors.toMap(Map.Entry::getKey, new Function<Map.Entry<String, Object>, T>() {
                    @Override
                    public T apply(Map.Entry<String, Object> entry) {
                        return (T) entry.getValue();
                    }
                }));
        return childMap;
    }

    @Override
    public <T> List<T> getInterfaceBYList(Class<T> interfaceClass) {
        List<T> list = factory.entrySet()
                .stream()
                .filter(entry -> {
                    return interfaceClass.isAssignableFrom(entry.getValue().getClass());
                })
                .map(entry -> (T)entry.getValue())
                .collect(Collectors.toList());
        return list;
    }
}
