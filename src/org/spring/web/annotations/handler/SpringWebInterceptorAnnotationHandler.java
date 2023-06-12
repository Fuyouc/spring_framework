package org.spring.web.annotations.handler;

import org.spring.Application;
import org.spring.annotations.autoconfig.Component;
import org.spring.core.handler.SpringBeanClassHandler;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;
import org.spring.web.annotations.WebInterceptor;
import org.spring.web.server.servlet.interceptor.SpringWebInterceptor;

@Component
public class SpringWebInterceptorAnnotationHandler implements SpringBeanClassHandler {
    @Override
    public void handler(String beanName, Object bean, Class<?> beanClass) {
        if (!ObjectUtils.isEmpty(beanClass.getAnnotation(WebInterceptor.class))){
            String prefix = beanClass.getAnnotation(WebInterceptor.class).value();
            if (StringUtils.isEmpty(prefix)) throw new RuntimeException("不允许SpringWebInterceptor指定的路径为空路径");
            prefix = StringUtils.getURIPrefix(prefix);
            Application.getApplicationContext().getFactory().getWebFactory().addWebInterceptor(prefix, (SpringWebInterceptor) bean);
        }
    }
}
