package org.spring.jdbc.annotation.handler;

import org.spring.Application;
import org.spring.annotations.autoconfig.AutoConfig;
import org.spring.core.handler.SpringBeanClassLoaderHandler;
import org.spring.jdbc.annotation.Mapper;
import org.spring.jdbc.proxy.DaoInterfaceProxy;
import org.spring.utils.global.StringUtils;

import java.lang.reflect.Proxy;

@AutoConfig
public class SpringJDBCMapperAnnotationHandler implements SpringBeanClassLoaderHandler {
    @Override
    public boolean handler(Class<?> targetClass) {
        if (targetClass.getAnnotation(Mapper.class) == null || !targetClass.isInterface()) return false;
        Object newProxyInstance = Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{targetClass},new DaoInterfaceProxy()); //创建代理类
        String name = targetClass.getAnnotation(Mapper.class).value();
        if (StringUtils.isEmpty(name)){
            name = targetClass.getName();
        }
        Application.getApplicationContext().getFactory().getBeanFactory().putBean(name,newProxyInstance);
        return true;
    }
}
