package org.spring.core.container;

import org.spring.core.container.bean.BeanFactory;
import org.spring.core.container.profile.ProfileFactory;
import org.spring.core.container.web.SpringWebFactory;

public interface SpringFactory {
    BeanFactory getBeanFactory();
    ProfileFactory getProfileFactory();
    SpringWebFactory getWebFactory();
}
