package org.spring;

import org.spring.core.context.ApplicationContext;

public final class Application {

    /**
     * 框架版本
     */
    public static final String VERSION = "2.0.5";

    private static Class<?> runClass;

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context){
        applicationContext = context;
    }

    public static void setRunClass(Class<?> runClass) {
        Application.runClass = runClass;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static Class<?> getRunClass() {
        return runClass;
    }
}
