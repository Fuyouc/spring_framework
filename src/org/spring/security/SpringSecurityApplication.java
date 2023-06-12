package org.spring.security;

import org.spring.Application;
import org.spring.annotations.autoconfig.Component;
import org.spring.core.container.SpringFactory;
import org.spring.core.container.bean.BeanFactory;
import org.spring.core.init.InitializingBean;
import org.spring.security.annotations.EnableSpringSecurity;
import org.spring.security.data.web.SpringSecurityWebInterceptor;
import org.spring.utils.global.ObjectUtils;

@Component
public final class SpringSecurityApplication implements InitializingBean {

    @Override
    public void init() {
        if (!ObjectUtils.isEmpty(Application.getRunClass().getAnnotation(EnableSpringSecurity.class))){
            SpringSecurityWebInterceptor springSecurityWebInterceptor = new SpringSecurityWebInterceptor();
            SpringFactory factory = Application.getApplicationContext().getFactory();
            BeanFactory beanFactory = factory.getBeanFactory();
            beanFactory.putBean("springSecurityWebInterceptor",springSecurityWebInterceptor);
            factory.getWebFactory().addWebInterceptor("/",springSecurityWebInterceptor);

        }
    }
}
