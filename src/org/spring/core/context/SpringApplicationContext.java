package org.spring.core.context;

import org.spring.core.container.SpringApplicationFactory;
import org.spring.core.container.SpringFactory;
import org.spring.core.container.bean.BeanFactory;
import org.spring.core.container.bean.SpringApplicationBeanFactory;
import org.spring.core.container.profile.ProfileFactory;
import org.spring.core.container.profile.SpringApplicationProfileFactory;
import org.spring.core.container.web.HttpRequestMappingFactory;
import org.spring.core.container.web.SpringWebComponentFactory;
import org.spring.core.container.web.SpringWebFactory;
import org.spring.core.container.web.SpringWebHttpRequestMappingFactory;
import org.spring.utils.global.StringUtils;

public final class SpringApplicationContext implements ApplicationContext{

    SpringFactory factory;

    public SpringApplicationContext() {
        factory = new SpringApplicationFactory();
    }

    @Override
    public SpringFactory getFactory() {
        return factory;
    }
}
