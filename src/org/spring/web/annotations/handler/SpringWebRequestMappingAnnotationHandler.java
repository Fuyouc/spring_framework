package org.spring.web.annotations.handler;

import org.spring.Application;
import org.spring.annotations.autoconfig.Component;
import org.spring.annotations.autoconfig.Controller;
import org.spring.core.container.web.HttpRequestMappingFactory;
import org.spring.core.container.web.SpringWebRequestMappingObject;
import org.spring.core.handler.SpringBeanMethodHandler;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;
import org.spring.web.annotations.request.RequestMapping;

import java.lang.reflect.Method;

@Component
public class SpringWebRequestMappingAnnotationHandler implements SpringBeanMethodHandler {
    @Override
    public void handler(String beanName, Object bean, Method method) {
        if (!ObjectUtils.isEmpty(bean.getClass().getAnnotation(Controller.class)) && !ObjectUtils.isEmpty(method.getAnnotation(RequestMapping.class))){
            Controller controller = bean.getClass().getAnnotation(Controller.class);
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            String address = mapping.value(); //获取请求地址
            address = address.charAt(0) == '/' ? address : "/" + address;
            String pre = controller.prefix();
            if (!StringUtils.isEmpty(pre)){
                pre = pre.charAt(0) == '/' ? pre : "/" + pre;
                address = pre + address;
            }
            HttpRequestMappingFactory httpRequestMappingFactory = Application.getApplicationContext().getFactory().getWebFactory().getHttpRequestMappingFactory();
            if (!httpRequestMappingFactory.containsURI(address)) {
                httpRequestMappingFactory.add(address,new SpringWebRequestMappingObject(bean,method,mapping.method()));
            }else {
                throw new RuntimeException("servlet 映射路径重复("+address+")，发生在 " + bean.getClass().getName() + " "  + method.getName() + "() 上");
            }
        }
    }
}
