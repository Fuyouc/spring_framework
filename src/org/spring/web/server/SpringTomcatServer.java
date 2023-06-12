package org.spring.web.server;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.spring.Application;
import org.spring.annotations.ConditionalOnMissingBean;
import org.spring.annotations.PostConstruct;
import org.spring.annotations.autoconfig.Autowired;
import org.spring.web.server.config.SpringServerConfiguration;
import org.spring.web.server.servlet.BaseServlet;

import java.lang.reflect.Method;

/**
 * Spring底层默认的Web服务器（采用Tomcat进行实现）
 */
@ConditionalOnMissingBean(SpringWebServerManager.class)
public class SpringTomcatServer implements SpringWebServerManager{

    @Autowired
    private SpringServerConfiguration configuration;
    private Tomcat tomcat;

    @PostConstruct
    private void init(){
        System.setProperty("catalina.home", "");
        System.setProperty("catalina.base", "");
        System.setProperty("java.util.logging.config.file", "/path/to/logging.properties");
        tomcat = new Tomcat();
        tomcat.setPort(configuration.getPort());
        StandardContext context = new StandardContext();
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());
        tomcat.getHost().addChild(context);
        BaseServlet bean = Application.getApplicationContext().getFactory().getBeanFactory().getBean(BaseServlet.class.getName());
        tomcat.addServlet("","baseServlet",bean);
        context.addServletMappingDecoded("/*","baseServlet");
    }


    @Override
    public void start() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            // 加载PackageInfo类
            Class<?> packageInfoClass = classLoader.loadClass("org.apache.catalina.util.ServerInfo");
            // 调用getServerInfo方法获取服务器信息
            Method method = packageInfoClass.getDeclaredMethod("getServerInfo");
            System.out.println("Starting Servlet engine: [" + method.invoke(null) + "]");
            System.out.println(getServerName() + " initialized with port(s): "+configuration.getPort()+" (http)");
            tomcat.start();
            System.out.println(getServerName() + " started on port(s): "+configuration.getPort()+" (http)");
        }catch (Exception e){
            e.printStackTrace();
        }
        new Thread(){
            @Override
            public void run() {
                tomcat.getServer().await();
            }
        }.start();
    }

    @Override
    public void close() {

    }

    @Override
    public String getServerName() {
        return "Tomcat";
    }
}
