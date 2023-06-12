package org.spring;

import org.spring.core.context.ApplicationContext;
import org.spring.core.context.SpringApplicationContext;
import org.spring.core.starter.SpringApplicationStarter;
import org.spring.core.starter.SpringWebFrameworkStarter;

public class SpringApplication {
    public static ApplicationContext run(Class<?> runClass,String... args){
        ApplicationContext context = new SpringApplicationContext();
        SpringWebFrameworkStarter starter = new SpringApplicationStarter();
        Application.setApplicationContext(context);
        Application.setRunClass(runClass);
        starter.run(args);
        return context;
    }
}
