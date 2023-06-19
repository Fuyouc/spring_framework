package org.spring.core.starter;

import org.spring.Application;
import org.spring.SpringApplication;
import org.spring.annotations.SpringBootApplication;
import org.spring.core.container.files.FileFactory;
import org.spring.core.container.files.SpringFile;
import org.spring.core.handler.SpringFileHandler;
import org.spring.core.init.SpringBeanInit;
import org.spring.core.loader.SpringFrameWorkClassLoader;
import org.spring.core.resources.SpringFrameworkResourceManager;
import org.spring.core.scanner.SpringWebClassScanner;
import org.spring.core.scanner.child.JarEnvironmentClassScanner;
import org.spring.core.scanner.child.LocalEnvironmentClassScanner;
import org.spring.utils.environment.EnvironmentUtils;
import org.spring.web.server.SpringWebServerManager;

import java.util.List;
import java.util.Map;

public class SpringApplicationStarter implements SpringWebFrameworkStarter{

    @Override
    public void run(String[] args) {
        checkEnvironment();
        classLoad();
        loadResource();
        init();
        log();
        startServer();
    }

    private void log() {
        System.out.println("String Framework（v"+Application.VERSION+"）");
        System.out.println("Starting "+Application.getRunClass().getSimpleName()+" using Java "+System.getProperty("java.version")+" on");
    }

    private void startServer() {
        SpringWebServerManager serverManager = Application.getApplicationContext().getFactory().getBeanFactory().getBean(SpringWebServerManager.class);
        if (serverManager != null) {
            serverManager.start();
        }
    }

    private void loadResource() {
        List<SpringFrameworkResourceManager> resourceManagers = Application.getApplicationContext().getFactory().getBeanFactory().getInterfaceBYList(SpringFrameworkResourceManager.class);
        for (SpringFrameworkResourceManager resourceManager : resourceManagers) {
            resourceManager.handler();
        }

        FileFactory fileFactory = Application.getApplicationContext().getFactory().getFileFactory();
        for (SpringFile file : fileFactory.getFiles()) {
            h:for (SpringFileHandler springFileHandler : Application.getApplicationContext().getFactory().getBeanFactory().getInterfaceBYList(SpringFileHandler.class)) {
                if (springFileHandler.handler(file)){
                    break h;
                }
            }
        }
    }

    private void init() {
        List<Map.Entry<String, Object>> beanList = Application.getApplicationContext().getFactory().getBeanFactory().getBeanList();
        for (Map.Entry<String, Object> bean : beanList) {
            SpringBeanInit.initClass(bean.getKey(),bean.getValue());
        }

        for (Map.Entry<String, Object> bean : beanList) {
            SpringBeanInit.initField(bean.getKey(),bean.getValue());
        }

        for (Map.Entry<String, Object> bean : beanList) {
            SpringBeanInit.initMethod(bean.getKey(),bean.getValue());
        }
    }

    /**
     * 校验缓解
     */
    private void checkEnvironment(){
        Class<?> runClass = Application.getRunClass();
        if (runClass.getAnnotation(SpringBootApplication.class) == null){
            throw new RuntimeException("找不到启动类，建议为 " + runClass.getName() + " 标记上@SpringBootApplication");
        }else {
            if ("".equals(runClass.getPackage().getName())){
                throw new RuntimeException("请保证项目有包名（如：com.xxx），如果直接在src目录中创建该启动类，则无法扫描到底下的其他类");
            }
        }
    }

    private void classLoad(){
        classLoad(Application.getRunClass());
        classLoad(SpringApplication.class);
        SpringFrameWorkClassLoader.classLoad();
    }

    private void classLoad(Class<?> runClass){
        SpringWebClassScanner scanner;
        if (EnvironmentUtils.isRunningFromJar(runClass)){
            scanner = new JarEnvironmentClassScanner();
        }else {
            scanner = new LocalEnvironmentClassScanner();
        }
        scanner.scan(runClass);
    }

    @Override
    public void close() {

    }
}
