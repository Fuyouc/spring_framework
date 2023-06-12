package org.spring.core.context;

import org.spring.core.container.SpringFactory;
import org.spring.core.container.bean.BeanFactory;
import org.spring.core.container.profile.ProfileFactory;
import org.spring.core.container.web.HttpRequestMappingFactory;
import org.spring.core.container.web.SpringWebFactory;

public interface ApplicationContext {
    SpringFactory getFactory();
}
