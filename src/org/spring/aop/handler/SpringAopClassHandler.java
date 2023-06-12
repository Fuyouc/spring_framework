package org.spring.aop.handler;

import com.sun.nio.sctp.HandlerResult;
import org.spring.Application;
import org.spring.annotations.autoconfig.AutoConfig;
import org.spring.annotations.autoconfig.Component;
import org.spring.aop.annotation.Advice;
import org.spring.aop.proxy.AopProxyHandler;
import org.spring.core.handler.SpringBeanClassHandler;
import org.spring.core.handler.SpringBeanClassLoaderHandler;
import org.spring.utils.global.ObjectUtils;

import javax.swing.*;

@Component
public class SpringAopClassHandler implements SpringBeanClassHandler {
    @Override
    public void handler(String beanName, Object bean, Class<?> beanClass) {
        Advice advice = beanClass.getAnnotation(Advice.class);
        if (!ObjectUtils.isEmpty(advice)){
            if (beanClass.getInterfaces().length > 0){
                AopProxyHandler proxyHandler = new AopProxyHandler(bean);
                try {
                    Object proxy = proxyHandler.getProxy(); //创建代理类
                    Application.getApplicationContext().getFactory().getBeanFactory().putBean(beanName,proxy);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                throw new RuntimeException(beanClass.getName() + "必须实现某个接口才可以使用动态代理！");
            }
        }
    }
}
