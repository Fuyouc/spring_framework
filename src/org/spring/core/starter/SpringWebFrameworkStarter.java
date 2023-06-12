package org.spring.core.starter;

/**
 * SpringWeb框架的启动器
 */
public interface SpringWebFrameworkStarter {
    /**
     * 运行框架
     */
    void run(String[] args);

    /**
     * 关闭框架
     */
    void close();
}
