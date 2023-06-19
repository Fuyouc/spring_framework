package org.spring.core.container;

import org.spring.core.container.bean.BeanFactory;
import org.spring.core.container.bean.SpringApplicationBeanFactory;
import org.spring.core.container.files.FileFactory;
import org.spring.core.container.files.SpringFileFactory;
import org.spring.core.container.profile.ProfileFactory;
import org.spring.core.container.profile.SpringApplicationProfileFactory;
import org.spring.core.container.web.SpringWebComponentFactory;
import org.spring.core.container.web.SpringWebFactory;

public class SpringApplicationFactory implements SpringFactory{

    private BeanFactory beanFactory;
    private ProfileFactory profileFactory;
    private SpringWebFactory webFactory;

    private FileFactory fileFactory;

    public SpringApplicationFactory() {
        beanFactory = new SpringApplicationBeanFactory();
        profileFactory = new SpringApplicationProfileFactory();
        webFactory = new SpringWebComponentFactory();
        fileFactory = new SpringFileFactory();
    }

    @Override
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public ProfileFactory getProfileFactory() {
        return profileFactory;
    }

    @Override
    public SpringWebFactory getWebFactory() {
        return webFactory;
    }

    @Override
    public FileFactory getFileFactory() {
        return fileFactory;
    }
}
